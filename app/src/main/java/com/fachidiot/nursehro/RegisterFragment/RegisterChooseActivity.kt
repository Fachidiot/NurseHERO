package com.fachidiot.nursehro.RegisterFragment

import android.content.Intent
import android.os.Bundle
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


class RegisterChooseActivity : AppCompatActivity() {
    private var nurse : Boolean = false
    private var sex : Boolean = true
    private var age : Int = 0

    private var region : String? = ""
    private var sigungu : String? = ""
    private var dong : String? = ""
    private var location : Boolean = false

    private lateinit var rvHorizontalPicker: RecyclerView
    private lateinit var tvSelectedItem: TextView

    private val data = (15..50).toList().map { it.toString() } as ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_choose)
        setTvSelectedItem()
        setHorizontalPicker()

        City.adapter = ArrayAdapter.createFromResource(this, R.array.spinner_region, android.R.layout.simple_spinner_dropdown_item)
        Gu.adapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_seoul, android.R.layout.simple_spinner_dropdown_item)
        Dong.adapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_seoul_gwanak, android.R.layout.simple_spinner_dropdown_item)
        City.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?)
            {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                when (position) {
                    //서울특별시
                    1 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_seoul, android.R.layout.simple_spinner_dropdown_item) }
                    //부산
                    2 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_busan, android.R.layout.simple_spinner_dropdown_item) }
                    //대구
                    3 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_daegu, android.R.layout.simple_spinner_dropdown_item) }
                    //인천
                    4 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_incheon, android.R.layout.simple_spinner_dropdown_item) }
                    //광주
                    5 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_gwangju, android.R.layout.simple_spinner_dropdown_item) }
                    //대전
                    6 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_daejeon, android.R.layout.simple_spinner_dropdown_item) }
                    //울산
                    7 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_ulsan, android.R.layout.simple_spinner_dropdown_item) }
                    //세종
                    8 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_sejong, android.R.layout.simple_spinner_dropdown_item) }
                    //경기도
                    9 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_gyeonggi, android.R.layout.simple_spinner_dropdown_item) }
                    //강원도
                    10 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_gangwon, android.R.layout.simple_spinner_dropdown_item) }
                    //충북
                    11 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_chung_buk, android.R.layout.simple_spinner_dropdown_item) }
                    //충남
                    12 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_chung_nam, android.R.layout.simple_spinner_dropdown_item) }
                    //전북
                    13 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_jeon_buk, android.R.layout.simple_spinner_dropdown_item) }
                    //전남
                    14 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_jeon_nam, android.R.layout.simple_spinner_dropdown_item) }
                    //경북
                    15 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_gyeong_buk, android.R.layout.simple_spinner_dropdown_item) }
                    //경남
                    16 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_gyeong_nam, android.R.layout.simple_spinner_dropdown_item) }
                    //제주
                    17 -> { Gu.adapter = ArrayAdapter.createFromResource(baseContext, R.array.spinner_region_jeju, android.R.layout.simple_spinner_dropdown_item) }
                    else -> { }
                }

                region = City.selectedItem.toString()
            }
        }
        Gu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                // 서울특별시 선택시
                if (City.selectedItemPosition == 1 && Gu.selectedItemPosition > -1) {
                    Dong.visibility = View.VISIBLE
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

                    sigungu = Gu.selectedItem.toString()
                } else {
                    Dong.visibility = View.INVISIBLE
                    sigungu = Gu.selectedItem.toString()
                    location = true
                }
            }
        }
        Dong.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                if (City.selectedItemPosition == 1 && Gu.selectedItemPosition > -1) {
                    dong = Dong.selectedItem.toString()
                    location = true
                }
            }
        }

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
            Toast.makeText(this, "You have to choose position", Toast.LENGTH_SHORT).show()
        }
        else if (!CheckBox_Man.isChecked && !CheckBox_Woman.isChecked) {
            Toast.makeText(this, "You have to pick sex", Toast.LENGTH_SHORT).show()
        }
        else if (!location) {
            Toast.makeText(this, "You have to enter your location", Toast.LENGTH_SHORT).show()
        }
        else {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("nurse", nurse)
            intent.putExtra("sex", sex)
            intent.putExtra("location", "$region/$sigungu/$dong")
            intent.putExtra("age", age)

            startActivity(intent)
        }
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