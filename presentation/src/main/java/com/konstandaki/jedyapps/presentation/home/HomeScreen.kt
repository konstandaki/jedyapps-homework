package com.konstandaki.jedyapps.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.konstandaki.jedyapps.domain.entity.Movie
import com.konstandaki.jedyapps.presentation.R
import com.konstandaki.jedyapps.sdk.ads.AdsSdk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenDetails: (Movie) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val pagingItems = viewModel.movies.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_omdb_search)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(12.dp)
        ) {
            SearchField(
                value = viewState.query,
                onValueChange = viewModel::onQueryChange,
                onClear = { viewModel.onQueryChange("") }
            )
            Spacer(Modifier.height(12.dp))

            MoviesList(
                movies = pagingItems,
                canSearch = viewState.canSearch,
                onOpenDetails = onOpenDetails
            )
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(stringResource(R.string.hint_search_movies)) },
        singleLine = true,
        trailingIcon = {
            if (value.isNotBlank()) {
                TextButton(onClick = onClear) {
                    Text(stringResource(R.string.action_clear))
                }
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus(force = true)
            }
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun MoviesList(
    movies: LazyPagingItems<Movie>,
    canSearch: Boolean,
    onOpenDetails: (Movie) -> Unit
) {
    if (!canSearch) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.msg_enter_min_chars))
        }
        return
    }

    val loadState = movies.loadState

    when {
        loadState.refresh is LoadState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }
        loadState.refresh is LoadState.Error -> {
            val e = (loadState.refresh as LoadState.Error).error
            ErrorBox(message = e.message ?: "") { movies.retry() }
            return
        }
        movies.itemCount == 0 -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.msg_nothing_found))
            }
            return
        }
    }

    val has3 = movies.itemCount >= 3
    val adPositions = remember(has3) { if (has3) listOf(1, 3) else emptyList() }
    val totalCount = movies.itemCount + adPositions.size

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = totalCount,
            key = { uiIndex ->
                if (uiIndex in adPositions) {
                    "ad-$uiIndex"
                } else {
                    val baseIdx = uiIndex - adPositions.count { it < uiIndex }
                    val id = movies.peek(baseIdx)?.id ?: "placeholder"
                    "m-$id-$baseIdx"
                }
            }
        ) { uiIndex ->
            if (uiIndex in adPositions) {
                AdsSdk.native().Render(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp)
                )
            } else {
                val baseIdx = uiIndex - adPositions.count { it < uiIndex }
                movies[baseIdx]?.let { movie ->
                    MovieRow(
                        movie = movie,
                        onClick = { onOpenDetails(movie) }
                    )
                }
            }
        }
        item {
            when (val a = loadState.append) {
                is LoadState.Loading -> Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(Modifier.padding(12.dp))
                }
                is LoadState.Error -> Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AssistChip(
                        onClick = { movies.retry() },
                        label = {
                            Text(stringResource(
                                R.string.label_retry_with_message,
                                a.error.message ?: ""))
                        }
                    )
                }
                else -> Unit
            }
        }
    }
}

@Composable
private fun MovieRow(movie: Movie, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(movie.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("${movie.year} • ${movie.type}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ErrorBox(message: String, onRetry: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.msg_oops_with_message, message),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) { Text(stringResource(R.string.label_retry)) }
    }
}