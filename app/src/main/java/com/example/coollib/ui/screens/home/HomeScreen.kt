package com.example.coollib.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.coollib.R
import com.example.coollib.domain.model.Category
import com.example.coollib.ui.components.paintBookCover
import com.example.coollib.ui.mapper.toUiModel
import com.example.coollib.ui.model.BookItemUiModel
import com.example.coollib.ui.previewSupport.MockBooks
import com.example.coollib.ui.previewSupport.MockCategory
import com.example.coollib.ui.previewSupport.MockWishlist
import com.example.coollib.ui.screens.checkout.WishlistViewModel
import com.example.coollib.ui.theme.CoolLibTheme

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    wishlistViewModel: WishlistViewModel = hiltViewModel(),
    onCategoryClick: (Int) -> Unit,
    onBookClick: (Int) -> Unit
){
    val category by homeViewModel.category.collectAsStateWithLifecycle()
    val lastViewBooks by homeViewModel.lastViewBooks.collectAsStateWithLifecycle()
    val wishlist by wishlistViewModel.wishlist.collectAsStateWithLifecycle()
    val newestBooks by homeViewModel.newestBooks.collectAsStateWithLifecycle()

    HomeScreenContent(
        categoryList = category,
        lastViewBooks = lastViewBooks.map { it.toUiModel() },
        wishlist = wishlist.map { it.toUiModel() },
        newestBooks = newestBooks.map { it.toUiModel() },
        onCategoryClick = onCategoryClick,
        onBookClick = onBookClick
    )
}

data class HomeSection(
    val titleRes: Int,
    val books: List<BookItemUiModel>
)

@Composable
fun HomeScreenContent(
    categoryList: List<Category>,
    lastViewBooks: List<BookItemUiModel>,
    wishlist: List<BookItemUiModel>,
    newestBooks: List<BookItemUiModel>,
    onCategoryClick: (Int) -> Unit,
    onBookClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val sections = listOf(
        HomeSection(R.string.recently_viewed, lastViewBooks),
        HomeSection(R.string.wishlist_label, wishlist),
        HomeSection(R.string.new_arrival, newestBooks)
    )

    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {

        item {
            SectionTitle(R.string.explore_categories)
        }

        item {
            CategoryRow(
                categories = categoryList,
                onCategoryClick = onCategoryClick
            )
        }

        items(sections) { section ->

            SectionTitle(section.titleRes)

            BookRow(
                books = section.books,
                onBookClick = onBookClick
            )
        }
    }
}

@Composable
fun SectionTitle(
    titleRes: Int
) {
    Text(
        text = stringResource(titleRes),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun CategoryRow(
    categories: List<Category>,
    onCategoryClick: (Int) -> Unit
) {

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(categories) { category ->

            CategoryItem(
                category = category,
                onCategoryClick = { onCategoryClick(category.id) }
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onCategoryClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier.width(240.dp),
        onClick = { onCategoryClick(category.id) },
        shape = MaterialTheme.shapes.small
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = category.coverUrl,
                placeholder = paintBookCover(category.id.toString(),category.name),
                error = paintBookCover(category.id.toString(),category.name),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop,
                contentDescription = category.name
            )

            Text(
                text = category.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun BookRow(
    books: List<BookItemUiModel>,
    onBookClick: (Int) -> Unit
) {

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(books) { book ->

            BookItem(
                book = book,
                onBookClick = { onBookClick(book.id) }
            )
        }
    }
}

@Composable
fun BookItem(
    book: BookItemUiModel,
    onBookClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier.width(140.dp),
        onClick = { onBookClick(book.id) },
        shape = MaterialTheme.shapes.small
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = book.coverUrl,
                placeholder = paintBookCover(book.title, book.author),
                error = paintBookCover(book.title, book.author),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f),
                contentScale = ContentScale.Crop,
                contentDescription = book.title
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = book.title,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CoolLibTheme() {
        HomeScreenContent(
            categoryList = MockCategory.list,
            lastViewBooks = MockBooks.list.map { it.toUiModel() }.shuffled(),
            wishlist = MockWishlist.list.map { it.toUiModel() }.shuffled(),
            newestBooks = MockBooks.list.map { it.toUiModel() }.shuffled(),
            onCategoryClick = {},
            onBookClick = {})
    }
}
