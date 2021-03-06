package com.joshualorett.heterogeneousrecyclerviewadapter

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import kotlin.IllegalArgumentException

/**
 * This adapter will trigger ViewHolder creation and binding via its ViewHolderCreators and
 * ViewHolderBinders respectively.
 *
 * There only needs to be a single ViewHolderCreator for each type of layout supported.
 * The number of ViewHolderBinders should be in parity with the data set.
 *
 * Created by Joshua on 11/20/2016.
 */

class HeterogeneousRecyclerViewAdapter(private var viewHolderCreatorMap: SparseArrayCompat<ViewHolderCreator> = SparseArrayCompat()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /***
     * Get the data for your list. The ViewHolderBinders that bind data to your ViewHolders.
     */
    var binders: List<ViewHolderBinder<Any>> = emptyList()

    fun addCreator(creator: ViewHolderCreator) {
        if(viewHolderCreatorMap.containsKey(creator.id)) {
            throw IllegalArgumentException("A creator of type ${creator.id} already exists.")
        }
        viewHolderCreatorMap.put(creator.id, creator)
    }

    fun removeCreatorByViewType(viewType: Int) {
        viewHolderCreatorMap.remove(viewType)
    }

    /***
     * @throws IllegalArgumentException if a ViewHolderCreator was not found.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val creator = viewHolderCreatorMap.get(viewType) ?:
        throw IllegalArgumentException("Could not find a ViewHolderCreator with viewType $viewType.")
        return creator.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        binders[position].bind(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return binders[position].id
    }

    override fun getItemCount(): Int {
        return binders.size
    }
}