package lat.pam.kopidannyemil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class MenuItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val priceValue: Int,
    val imageResId: Int = R.drawable.menu_kopi_susu_gula_aren,
    var quantity: Int = 0,
)

class MenuAdapter(
    private val menuList: List<MenuItem>,
    private val onQuantityChanged: ((MenuItem, Int) -> Unit)? = null,
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPhoto: ImageView = itemView.findViewById(R.id.iv_menu_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_menu_name)
        val tvDesc: TextView = itemView.findViewById(R.id.tv_menu_desc)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_menu_price)
        val tvQuantity: TextView = itemView.findViewById(R.id.tv_quantity)
        val btnMinus: Button = itemView.findViewById(R.id.btn_minus)
        val btnPlus: Button = itemView.findViewById(R.id.btn_plus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = menuList[position]
        holder.ivPhoto.setImageResource(item.imageResId)
        holder.tvName.text = item.name
        holder.tvDesc.text = item.description
        holder.tvPrice.text = item.price
        holder.tvQuantity.text = item.quantity.toString()

        holder.btnMinus.setOnClickListener {
            if (item.quantity > 0) {
                item.quantity--
                holder.tvQuantity.text = item.quantity.toString()
                onQuantityChanged?.invoke(item, item.quantity)
            }
        }

        holder.btnPlus.setOnClickListener {
            item.quantity++
            holder.tvQuantity.text = item.quantity.toString()
            onQuantityChanged?.invoke(item, item.quantity)
        }
    }

    override fun getItemCount(): Int = menuList.size
}
