package lat.pam.kopidannyemil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class OrderAdapter(
    private var orderList: List<MenuItem>,
    private val onQuantityChanged: ((MenuItem, Int) -> Unit)? = null,
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPhoto: ImageView = itemView.findViewById(R.id.iv_order_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_order_name)
        val tvUnitInfo: TextView = itemView.findViewById(R.id.tv_order_unit_info)
        val tvSubtotal: TextView = itemView.findViewById(R.id.tv_order_subtotal)
        val tvQty: TextView = itemView.findViewById(R.id.tv_order_qty)
        val btnMinus: Button = itemView.findViewById(R.id.btn_order_minus)
        val btnPlus: Button = itemView.findViewById(R.id.btn_order_plus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = orderList[position]
        holder.ivPhoto.setImageResource(item.imageResId)
        holder.tvName.text = item.name

        val unitPriceStr = item.price.replace("• ", "").trim()
        holder.tvUnitInfo.text = holder.itemView.context.getString(
            R.string.order_unit_format,
            item.quantity,
            unitPriceStr,
        )
        holder.tvSubtotal.text = formatRupiah(item.priceValue * item.quantity)
        holder.tvQty.text = item.quantity.toString()

        holder.btnMinus.setOnClickListener {
            if (item.quantity > 0) {
                item.quantity--
                onQuantityChanged?.invoke(item, item.quantity)
            }
        }

        holder.btnPlus.setOnClickListener {
            item.quantity++
            onQuantityChanged?.invoke(item, item.quantity)
        }
    }

    override fun getItemCount(): Int = orderList.size

    fun updateData(newList: List<MenuItem>) {
        orderList = newList
        notifyDataSetChanged()
    }

    companion object {
        fun formatRupiah(amount: Int): String {
            val formatter = NumberFormat.getIntegerInstance(Locale.forLanguageTag("id-ID"))
            return "Rp ${formatter.format(amount)}"
        }
    }
}
