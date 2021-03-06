package com.fachidiot.nursehro.RegisterFragment

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.fachidiot.nursehro.R
import com.fachidiot.nursehro.ViewHolder.ScreenUtils
import com.fachidiot.nursehro.ViewHolder.SliderAdapter
import com.fachidiot.nursehro.ViewHolder.SliderLayoutManager
import kotlinx.android.synthetic.main.activity_register_choose.*
import kotlinx.android.synthetic.main.activity_register_choose.button_back


class RegisterChooseActivity : AppCompatActivity() {
    private var nurse : Boolean = false
    private var sex : Boolean = true
    private var age : Int = 0

    private var region : String? = ""
    private var sigungu : String? = ""
    private var dong : String? = ""
    private var detail : String? = ""
    private var lat : Double? = 0.0
    private var lng : Double? = 0.0
    private var location : Boolean = false

    private lateinit var rvHorizontalPicker: RecyclerView
    private lateinit var tvSelectedItem: TextView

    private val data = (15..50).toList().map { it.toString() } as ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_choose)
        setTvSelectedItem()
        setHorizontalPicker()

        City.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region, R.layout.spinner_textview)
        Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul, R.layout.spinner_textview)
        Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_gwanak, R.layout.spinner_textview)
        City.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?)
            {
                location = false
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                when (position) {
                    0 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_sigungu, android.R.layout.simple_spinner_dropdown_item) }
                    //???????????????
                    1 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    2 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_busan, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    3 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_daegu, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    4 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_incheon, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    5 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_gwangju, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    6 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_daejeon, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    7 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_ulsan, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    8 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_sejong, android.R.layout.simple_spinner_dropdown_item) }
                    //?????????
                    9 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_gyeonggi, android.R.layout.simple_spinner_dropdown_item) }
                    //?????????
                    10 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_gangwon, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    11 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_chung_buk, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    12 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_chung_nam, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    13 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_jeon_buk, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    14 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_jeon_nam, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    15 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_gyeong_buk, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    16 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_gyeong_nam, android.R.layout.simple_spinner_dropdown_item) }
                    //??????
                    17 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_jeju, android.R.layout.simple_spinner_dropdown_item) }
                    else -> { location = false }
                }

                region = City.selectedItem.toString()
                Log.d("Choose", region!!)
            }
        }
        Gu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?)
            {
                location = false
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                // ??????????????? ?????????
                if (City.selectedItemPosition == 1 && Gu.selectedItemPosition > 0) {
                    when (position) {
                        1 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_gangnam, android.R.layout.simple_spinner_dropdown_item)
                        2 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_gangdong, android.R.layout.simple_spinner_dropdown_item)
                        3 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_gangbuk, android.R.layout.simple_spinner_dropdown_item)
                        4 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_gangseo, android.R.layout.simple_spinner_dropdown_item)
                        5 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_gwanak, android.R.layout.simple_spinner_dropdown_item)
                        6 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_gwangjin, android.R.layout.simple_spinner_dropdown_item)
                        7 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_guro, android.R.layout.simple_spinner_dropdown_item)
                        8 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_geumcheon, android.R.layout.simple_spinner_dropdown_item)
                        9 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_nowon, android.R.layout.simple_spinner_dropdown_item)
                        10 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_dobong, android.R.layout.simple_spinner_dropdown_item)
                        11 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_dongdaemun, android.R.layout.simple_spinner_dropdown_item)
                        12 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_dongjag, android.R.layout.simple_spinner_dropdown_item)
                        13 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_mapo, android.R.layout.simple_spinner_dropdown_item)
                        14 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_seodaemun, android.R.layout.simple_spinner_dropdown_item)
                        15 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_seocho, android.R.layout.simple_spinner_dropdown_item)
                        16 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_seongdong, android.R.layout.simple_spinner_dropdown_item)
                        17 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_seongbuk, android.R.layout.simple_spinner_dropdown_item)
                        18 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_songpa, android.R.layout.simple_spinner_dropdown_item)
                        19 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_yangcheon, android.R.layout.simple_spinner_dropdown_item)
                        20 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_yeongdeungpo, android.R.layout.simple_spinner_dropdown_item)
                        21 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_yongsan, android.R.layout.simple_spinner_dropdown_item)
                        22 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_eunpyeong, android.R.layout.simple_spinner_dropdown_item)
                        23 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_jongno, android.R.layout.simple_spinner_dropdown_item)
                        24 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_jung, android.R.layout.simple_spinner_dropdown_item)
                        25 -> Dong.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul_jungnanggu, android.R.layout.simple_spinner_dropdown_item)
                    }
                    location = false
                    sigungu = Gu.selectedItem.toString()
                    Log.d("Choose", sigungu!!)
                    Dong.visibility = View.VISIBLE
                }
                else if (City.selectedItemPosition >= 1 && Gu.selectedItemPosition > 0) {
                    sigungu = Gu.selectedItem.toString()
                    Log.d("Choose", sigungu!!)
                    Dong.visibility = View.GONE
                    location = true
                }

            }
        }
        Dong.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?)
            {
                location = false
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                if (City.selectedItemPosition >= 1 && Gu.selectedItemPosition > 0) {
                    if (Dong.selectedItemPosition > 0) {
                        dong = Dong.selectedItem.toString()
                        Log.d("Choose", dong!!)
                        location = true
                    } else {
                        location = false
                    }
                } else {
                    location = false
                }
            }
        }
        Detail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable) {
                detail = Detail.text.toString()
            }
        })

        RelativeLayout_Next.setOnClickListener {
            checkChoice()
        }

        button_back.setOnClickListener {
            finish()
        }

        CheckBox_Nurse.setOnClickListener {
            CheckBox_Normal.isChecked = false
            nurse = true
        }

        CheckBox_Normal.setOnClickListener {
            CheckBox_Nurse.isChecked = false
            nurse = false
        }

        CheckBox_Man.setOnClickListener {
            CheckBox_Woman.isChecked = false
            sex = true
        }

        CheckBox_Woman.setOnClickListener {
            CheckBox_Man.isChecked = false
            sex = false
        }
    }

    private fun checkChoice() {
        if (!CheckBox_Normal.isChecked && !CheckBox_Nurse.isChecked) {
            Toast.makeText(this, "???????????? ????????? ??????????????????", Toast.LENGTH_SHORT).show()
        }
        else if (!CheckBox_Man.isChecked && !CheckBox_Woman.isChecked) {
            Toast.makeText(this, "????????? ??????????????????", Toast.LENGTH_SHORT).show()
        }
        else if (!location) {
            Toast.makeText(this, "????????? ??????????????????", Toast.LENGTH_SHORT).show()
        }
        else if (!checkDetail()) {
            Toast.makeText(baseContext, "????????? ???????????????.\n??????????????? ??????????????????", Toast.LENGTH_SHORT).show()
        }
        else {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("nurse", nurse)
            intent.putExtra("sex", sex)
            if (!dong.isNullOrEmpty())
                intent.putExtra("dong", "$dong")
            intent.putExtra("region", "$region")
            Log.d("Region", region!!)
            intent.putExtra("sigungu", "$sigungu")
            intent.putExtra("detail", "$detail")
            intent.putExtra("latitude", lat)
            intent.putExtra("longtitude", lng)
            intent.putExtra("age", age)

            startActivity(intent)
        }
    }

    private fun checkDetail() : Boolean {
        val geocoder = Geocoder(baseContext)

        if (!region.isNullOrEmpty() && !sigungu.isNullOrEmpty() && !dong.isNullOrEmpty())
        {
            val addresses: List<Address> = geocoder.getFromLocationName(region+sigungu+dong+detail, 3)
            if (addresses[0].postalCode == null)
                return false
            else {
                val address: Address = addresses[0]
                lat = address.latitude
                lng = address.longitude
                return true
            }
        }
        else if (!region.isNullOrEmpty() && !sigungu.isNullOrEmpty() && region != "???????????????")
        {
            val addresses: List<Address> = geocoder.getFromLocationName(region+sigungu+detail, 3)
            if (addresses[0].postalCode == null)
                return false
            else {
                val address: Address = addresses[0]
                lat = address.latitude
                lng = address.longitude
                return true
            }
        }
        return false
    }

    private fun setTvSelectedItem() {
        tvSelectedItem = findViewById(R.id.tv_selected_item)
    }

    private fun setHorizontalPicker() {
        rvHorizontalPicker = findViewById(R.id.rv_horizontal_picker)

        // Setting the padding such that the items will appear in the middle of the screen
        val padding: Int = ScreenUtils.getScreenWidth(this)/2 - ScreenUtils.dpToPx(this, 40)
        rvHorizontalPicker.setPadding(padding, 0, padding, 0)

        // Setting layout manager
        rvHorizontalPicker.layoutManager = SliderLayoutManager(this).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    tvSelectedItem.text = data[layoutPosition]
                    age = data[layoutPosition].toInt()
                }
            }
        }

        // Setting Adapter
        rvHorizontalPicker.adapter = SliderAdapter().apply {
            setData(data)
            callback = object : SliderAdapter.Callback {
                override fun onItemClicked(view: View) {
                    rvHorizontalPicker.smoothScrollToPosition(rvHorizontalPicker.getChildLayoutPosition(view))
                }
            }
        }
    }
}