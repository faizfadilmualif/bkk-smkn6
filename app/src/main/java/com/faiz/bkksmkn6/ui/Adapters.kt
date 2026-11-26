package com.faiz.bkksmkn6.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.faiz.bkksmkn6.R
import com.faiz.bkksmkn6.data.Berita
import com.faiz.bkksmkn6.data.Lowongan
import com.faiz.bkksmkn6.data.Perusahaan
import com.faiz.bkksmkn6.util.loadImage
import com.faiz.bkksmkn6.util.setTextOrHide
import com.faiz.bkksmkn6.util.tanggalIndo


class LowonganAdapter(private val onClick: (Lowongan) -> Unit ):
        ListAdapter<Lowongan, LowonganAdapter.VH>(Diff){

    class VH(view: View) : RecyclerView.ViewHolder(view){
        val ivLogo: ImageView = view.findViewById(R.id.ivLogo)
        val tvTitle: TextView =view.findViewById(R.id.tvTitle)
        val tvCompany: TextView = view.findViewById(R.id.tvCompany)
        val tvMeta: TextView = view.findViewById(R.id.tvMeta)
        val tvSalary: TextView = view.findViewById(R.id.tvSalary)
        val tvDeadline: TextView = view.findViewById(R.id.tvDeadline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= VH(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lowongan, parent, false)

    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val l = getItem(position)
        with(holder){
            ivLogo.loadImage(l.company?.logoUrl)
            tvTitle.text=l.title
            tvCompany.setTextOrHide(l.company?.name)
            tvMeta.setTextOrHide(
                listOfNotNull(l.type,l.location,l.field)
                    .filter { it.isNotBlank() }
                    .joinToString ( "•" )
            )
            tvSalary.setTextOrHide(l.salaryText)
            val batas = l.deadline.tanggalIndo()
            tvDeadline.setTextOrHide(
                when{
                    l.isOpen==false -> "Lowongan ditutup"
                    batas != null -> "batas lamaran $batas"
                    else -> null
                }
            )
            itemView.setOnClickListener { onClick(l) }
        }
    }
    object Diff : DiffUtil.ItemCallback<Lowongan>(){
        override fun areItemsTheSame(a: Lowongan, b: Lowongan) = a.id == b.id
        override fun areContentsTheSame(a: Lowongan, b: Lowongan) = a == b
        }

}
class BeritaAdapter(private val onClick: (Berita) -> Unit) :
        ListAdapter<Berita, BeritaAdapter.VH>(Diff){
    class VH(view: View) : RecyclerView.ViewHolder(view){
        val ivThumb: ImageView = view.findViewById(R.id.ivThumb)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvSummary: TextView = view.findViewById(R.id.tvSummary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= VH (
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_berita, parent, false)

        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val n = getItem(position)
        with(holder) {
            ivThumb.visibility = if (n.thumbUrl.isNullOrBlank()) View.GONE else View.VISIBLE
            if (!n.thumbUrl.isNullOrBlank()) ivThumb.loadImage(n.thumbUrl)
            tvTitle.text = n.title
            tvDate.setTextOrHide(n.publishedAt.tanggalIndo())
            tvSummary.setTextOrHide(n.summary)
            itemView.setOnClickListener { onClick(n) }
        }
        }
    object Diff: DiffUtil.ItemCallback<Berita>(){
        override fun areItemsTheSame(a: Berita, b: Berita) = a.id == b.id
        override fun areContentsTheSame(a: Berita, b: Berita) = a==b
        }
    }
class PerusahaanAdapter(private val  onClick: (Perusahaan) -> Unit):
        ListAdapter<Perusahaan, PerusahaanAdapter.VH>(Diff){

    class VH(view: View) : RecyclerView.ViewHolder(view){
        val ivLogo: ImageView = view.findViewById(R.id.ivLogo)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCity: TextView = view.findViewById(R.id.tvCity)
        val tvJobs: TextView = view.findViewById(R.id.tvJobs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= VH (
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_perusahaan,parent,false)
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p=getItem(position)
        with(holder) {
            ivLogo.loadImage(p.logoUrl)
            tvName.text = p.name
            tvCity.setTextOrHide(p.city)
            tvJobs.setTextOrHide(
                if (p.openJobsCount > 0) "${p.openJobsCount} lowongan dibuka" else
                    null
            )
            itemView.setOnClickListener { onClick(p) }

        }
    }
    object Diff: DiffUtil.ItemCallback<Perusahaan>(){
        override fun areItemsTheSame(a: Perusahaan, b: Perusahaan) = a.id == b.id
        override fun areContentsTheSame(a: Perusahaan, b: Perusahaan) = a==b


        }
    }
