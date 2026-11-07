package lat.pam.kopidannyemil

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    private enum class Tab { HOME, ORDER, PROFILE }

    private lateinit var containerHome: View
    private lateinit var containerOrder: View
    private lateinit var containerProfile: View

    private lateinit var navHome: TextView
    private lateinit var navOrder: TextView
    private lateinit var navProfile: TextView

    private lateinit var tvGreetingHome: TextView
    private lateinit var tvGreetingOrder: TextView
    private lateinit var tvGreetingProfile: TextView

    private lateinit var layoutEmptyOrder: View
    private lateinit var layoutOrderContent: View
    private lateinit var rvOrderItems: RecyclerView
    private lateinit var tvOrderTotalItems: TextView
    private lateinit var tvOrderGrandTotal: TextView
    private lateinit var btnProceedOrder: Button

    private lateinit var drinkAdapter: MenuAdapter
    private lateinit var foodAdapter: MenuAdapter
    private lateinit var orderAdapter: OrderAdapter
    private val drinkList = mutableListOf<MenuItem>()
    private val foodList = mutableListOf<MenuItem>()
    private val menuList = mutableListOf<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        initData()
        setupListeners()
        updateUserGreetings()

        selectTab(Tab.HOME)
    }

    override fun onResume() {
        super.onResume()
        updateUserGreetings()
    }

    private fun initViews() {
        containerHome = findViewById(R.id.container_home)
        containerOrder = findViewById(R.id.container_order)
        containerProfile = findViewById(R.id.container_profile)

        navHome = findViewById(R.id.nav_home)
        navOrder = findViewById(R.id.nav_order)
        navProfile = findViewById(R.id.nav_profile)

        tvGreetingHome = findViewById(R.id.tv_greeting_home)
        tvGreetingOrder = findViewById(R.id.tv_greeting_order)
        tvGreetingProfile = findViewById(R.id.tv_greeting_profile)

        layoutEmptyOrder = findViewById(R.id.layout_empty_order)
        layoutOrderContent = findViewById(R.id.layout_order_content)
        rvOrderItems = findViewById(R.id.rv_order_items)
        tvOrderTotalItems = findViewById(R.id.tv_order_total_items)
        tvOrderGrandTotal = findViewById(R.id.tv_order_grand_total)
        btnProceedOrder = findViewById(R.id.btn_proceed_order)
    }

    private fun initData() {
        drinkList.clear()
        drinkList.addAll(
            listOf(
                MenuItem(
                    id = 1,
                    name = "Kopi Susu Gula Aren",
                    description = "Espresso, Susu, Gula aren",
                    price = "Rp 22.000",
                    priceValue = 22000,
                    imageResId = R.drawable.menu_kopi_susu_gula_aren,
                ),
                MenuItem(
                    id = 4,
                    name = "Cappucino",
                    description = "Espresso, Foam susu lembut",
                    price = "Rp 25.000",
                    priceValue = 25000,
                    imageResId = R.drawable.menu_cappucino,
                ),
                MenuItem(
                    id = 5,
                    name = "Matcha Latte",
                    description = "Matcha jepang, Susu segar",
                    price = "Rp 27.000",
                    priceValue = 27000,
                    imageResId = R.drawable.menu_matcha_latte,
                ),
            )
        )

        foodList.clear()
        foodList.addAll(
            listOf(
                MenuItem(
                    id = 2,
                    name = "Donat Coklat",
                    description = "Donat empuk, Glaze coklat",
                    price = "Rp 14.000",
                    priceValue = 14000,
                    imageResId = R.drawable.menu_donat_coklat,
                ),
                MenuItem(
                    id = 3,
                    name = "Sourdough Loaf",
                    description = "Roti fermentasi alami",
                    price = "Rp 35.000",
                    priceValue = 35000,
                    imageResId = R.drawable.menu_sourdough_loaf,
                ),
            )
        )

        menuList.clear()
        menuList.addAll(drinkList + foodList)

        val rvMinuman = findViewById<RecyclerView>(R.id.rv_minuman)
        rvMinuman.layoutManager = LinearLayoutManager(this)
        drinkAdapter = MenuAdapter(drinkList) { _, _ ->
            foodAdapter.notifyDataSetChanged()
            refreshOrderState()
        }
        rvMinuman.adapter = drinkAdapter

        val rvMakanan = findViewById<RecyclerView>(R.id.rv_makanan)
        rvMakanan.layoutManager = LinearLayoutManager(this)
        foodAdapter = MenuAdapter(foodList) { _, _ ->
            drinkAdapter.notifyDataSetChanged()
            refreshOrderState()
        }
        rvMakanan.adapter = foodAdapter

        rvOrderItems.layoutManager = LinearLayoutManager(this)
        orderAdapter = OrderAdapter(emptyList()) { _, _ ->
            drinkAdapter.notifyDataSetChanged()
            foodAdapter.notifyDataSetChanged()
            refreshOrderState()
        }
        rvOrderItems.adapter = orderAdapter
    }

    private fun setupListeners() {
        val onHomeClick = View.OnClickListener { selectTab(Tab.HOME) }
        val onOrderClick = View.OnClickListener { selectTab(Tab.ORDER) }
        val onProfileClick = View.OnClickListener { selectTab(Tab.PROFILE) }

        findViewById<View>(R.id.tab_home)?.setOnClickListener(onHomeClick)
        findViewById<View>(R.id.tab_order)?.setOnClickListener(onOrderClick)
        findViewById<View>(R.id.tab_profile)?.setOnClickListener(onProfileClick)
        navHome.setOnClickListener(onHomeClick)
        navOrder.setOnClickListener(onOrderClick)
        navProfile.setOnClickListener(onProfileClick)

        // Empty Order browse button
        findViewById<Button>(R.id.btn_browse_menu)?.setOnClickListener {
            selectTab(Tab.HOME)
        }

        // Proceed to Address Screen
        btnProceedOrder.setOnClickListener {
            val selectedItems = menuList.filter { it.quantity > 0 }
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Silakan pilih menu terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val grandTotal = selectedItems.sumOf { it.priceValue * it.quantity }
            val totalQuantity = selectedItems.sumOf { it.quantity }
            val orderSummary = selectedItems.joinToString(", ") { "${it.name} (${it.quantity}x)" }

            val intent = Intent(this, AddressActivity::class.java).apply {
                putExtra("TOTAL_PRICE", grandTotal)
                putExtra("TOTAL_ITEMS", totalQuantity)
                putExtra("ORDER_SUMMARY", orderSummary)
            }
            startActivity(intent)
        }

        // Profile tab actions
        val etProfileName = findViewById<EditText>(R.id.et_profile_name)
        findViewById<Button>(R.id.btn_save_profile)?.setOnClickListener {
            val name = etProfileName.text.toString().trim()
            if (name.isNotEmpty()) {
                UserSession.saveUser(this, name)
                updateUserGreetings()
                Toast.makeText(this, "Profil berhasil disimpan!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Silakan masukkan nama lengkap", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btn_logout)?.setOnClickListener {
            UserSession.clear(this)
            val intent = Intent(this, SignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun updateUserGreetings() {
        val fullName = UserSession.getUserName(this)
        val firstName = UserSession.getFirstName(this)
        val greeting = getString(R.string.greeting_format, firstName)
        tvGreetingHome.text = greeting
        tvGreetingOrder.text = greeting
        tvGreetingProfile.text = greeting
        findViewById<EditText>(R.id.et_profile_name)?.setText(fullName)
    }

    private fun selectTab(tab: Tab) {
        containerHome.visibility = if (tab == Tab.HOME) View.VISIBLE else View.GONE
        containerOrder.visibility = if (tab == Tab.ORDER) View.VISIBLE else View.GONE
        containerProfile.visibility = if (tab == Tab.PROFILE) View.VISIBLE else View.GONE

        setNavTabStyle(navHome, tab == Tab.HOME)
        setNavTabStyle(navOrder, tab == Tab.ORDER)
        setNavTabStyle(navProfile, tab == Tab.PROFILE)

        if (tab == Tab.ORDER) {
            refreshOrderState()
        }
    }

    private fun setNavTabStyle(textView: TextView, isActive: Boolean) {
        if (isActive) {
            textView.setBackgroundResource(R.drawable.bg_nav_active)
            textView.setTextColor(ContextCompat.getColor(this, R.color.white))
            textView.setTypeface(null, Typeface.BOLD)
            textView.setPadding(dpToPx(24), dpToPx(12), dpToPx(24), dpToPx(12))
        } else {
            textView.background = null
            textView.setTextColor(ContextCompat.getColor(this, R.color.grey_nav_inactive))
            textView.setTypeface(null, Typeface.NORMAL)
            textView.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12))
        }
    }

    private fun refreshOrderState() {
        val selectedItems = menuList.filter { it.quantity > 0 }
        val totalQuantity = selectedItems.sumOf { it.quantity }
        val grandTotal = selectedItems.sumOf { it.priceValue * it.quantity }

        if (selectedItems.isEmpty()) {
            layoutEmptyOrder.visibility = View.VISIBLE
            layoutOrderContent.visibility = View.GONE
        } else {
            layoutEmptyOrder.visibility = View.GONE
            layoutOrderContent.visibility = View.VISIBLE
            orderAdapter.updateData(selectedItems)
            tvOrderTotalItems.text = getString(R.string.item_count_format, totalQuantity)
            tvOrderGrandTotal.text = OrderAdapter.formatRupiah(grandTotal)
        }

        navOrder.text = if (totalQuantity > 0) "Order ($totalQuantity)" else getString(R.string.order)
    }

    private fun dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()
}
