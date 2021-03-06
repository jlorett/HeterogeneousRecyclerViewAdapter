package com.joshualorett.heterogeneousrecyclerviewadapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.*

class HeterogeneousRecyclerViewAdapterTest {
    @Test
    fun adapterDefaultsToEmpty() {
        val adapter = HeterogeneousRecyclerViewAdapter()
        assertEquals(0, adapter.itemCount)
    }

    @Test
    fun defaultHasEmptyBinders() {
        assertTrue(HeterogeneousRecyclerViewAdapter().binders.isEmpty())
    }

    @Test(expected = IllegalArgumentException::class)
    fun failIfWeTryToAddDuplicateKeys() {
        val mockCreator = mock(ViewHolderCreator::class.java)
        `when`(mockCreator.id).thenReturn(1)
        val viewHolderCreatorMap = SparseArrayCompat<ViewHolderCreator>()
        viewHolderCreatorMap.append(mockCreator.id, mockCreator)
        val adapter = HeterogeneousRecyclerViewAdapter(viewHolderCreatorMap)
        adapter.addCreator(mockCreator)
    }

    @Test
    fun removeCreator() {
        val mockCreator = mock(ViewHolderCreator::class.java)
        `when`(mockCreator.id).thenReturn(1)
        val viewHolderCreatorMap = SparseArrayCompat<ViewHolderCreator>()
        viewHolderCreatorMap.append(mockCreator.id, mockCreator)
        val adapter = HeterogeneousRecyclerViewAdapter(viewHolderCreatorMap)
        adapter.removeCreatorByViewType(1)
        assertEquals(0, adapter.itemCount)
    }

    @Test(expected = IllegalArgumentException::class)
    fun failIfViewTypeDoesNotExistWhileCreatingViewHolder() {
        val adapter = HeterogeneousRecyclerViewAdapter()
        adapter.onCreateViewHolder(mock(ViewGroup::class.java), 1)
    }

    @Test
    fun bindCallsBinder() {
        val mockBinder = mock(ViewHolderBinder::class.java) as ViewHolderBinder<Nothing>
        `when`(mockBinder.id).thenReturn(1)
        val mockCreator = mock(ViewHolderCreator::class.java)
        `when`(mockCreator.id).thenReturn(1)
        val viewHolderCreatorMap = SparseArrayCompat<ViewHolderCreator>()
        viewHolderCreatorMap.append(mockCreator.id, mockCreator)
        val adapter = HeterogeneousRecyclerViewAdapter(viewHolderCreatorMap)
        adapter.binders = listOf(mockBinder)
        val mockViewHolder = mock(RecyclerView.ViewHolder::class.java)
        adapter.onBindViewHolder(mockViewHolder, 0)
        verify(mockBinder).bind(mockViewHolder)
    }

    @Test
    fun getItemViewTypeReturnsBindersViewType() {
        val mockBinder = mock(ViewHolderBinder::class.java) as ViewHolderBinder<Nothing>
        `when`(mockBinder.id).thenReturn(1)
        val mockCreator = mock(ViewHolderCreator::class.java)
        `when`(mockCreator.id).thenReturn(1)
        val viewHolderCreatorMap = SparseArrayCompat<ViewHolderCreator>()
        viewHolderCreatorMap.append(mockCreator.id, mockCreator)
        val adapter = HeterogeneousRecyclerViewAdapter(viewHolderCreatorMap)
        adapter.binders = listOf(mockBinder)
        assertEquals(1, adapter.getItemViewType(0))
    }

    @Test
    fun getItemCountReturnsNumberOfBinders() {
        val mockBinder = mock(ViewHolderBinder::class.java) as ViewHolderBinder<Nothing>
        `when`(mockBinder.id).thenReturn(1)
        val mockCreator = mock(ViewHolderCreator::class.java)
        `when`(mockCreator.id).thenReturn(1)
        val viewHolderCreatorMap = SparseArrayCompat<ViewHolderCreator>()
        viewHolderCreatorMap.append(mockCreator.id, mockCreator)
        val adapter = HeterogeneousRecyclerViewAdapter(viewHolderCreatorMap)
        val mockBinderList = listOf(mockBinder)
        adapter.binders = mockBinderList
        assertEquals(mockBinderList.size, adapter.itemCount)
    }
}