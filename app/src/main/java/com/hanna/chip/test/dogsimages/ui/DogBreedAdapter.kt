package com.hanna.chip.test.dogsimages.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hanna.chip.test.dogsimages.R
import com.hanna.chip.test.dogsimages.entities.DogBreed

class DogBreedAdapter(val onItemClick: (dogBreed: DogBreed) -> Unit) :
    ListAdapter<DogBreed, DogBreedViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<DogBreed>() {
            override fun areItemsTheSame(oldItem: DogBreed, newItem: DogBreed): Boolean {
                return oldItem.breedType == newItem.breedType
            }

            override fun areContentsTheSame(oldItem: DogBreed, newItem: DogBreed): Boolean {
                return oldItem.parentBreedType == newItem.parentBreedType
                //no need to check the type, since its sbeen checked under the areItemsTheSame
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogBreedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_dog_breed, parent, false)
        return DogBreedViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: DogBreedViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }
}


class DogBreedViewHolder(itemView: View, val onItemClick: (dogBreed: DogBreed) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    private val typeTextView: TextView = itemView.findViewById(R.id.tv)

    fun bindData(data: DogBreed) {
        typeTextView.text = data.breedTypeDescription
        itemView.setOnClickListener { onItemClick(data) }
    }
}