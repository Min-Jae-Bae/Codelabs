/*
 * Copyright 2020, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*활동 수명 주기
* 활동 수명 주기는 활동이 이동하는 일련의 상태입니다.
* 활동 수명 주기는 활동이 처음 만들어질 때 시작되고 활동이 소멸될 때 종료됩니다.
* 사용자가 활동 간에 그리고 앱 안팎으로 이동할 때 각 활동은 활동 수명 주기의 상태 간에 이동합니다.
* 활동 수명 주기의 각 상태에는 Activity 클래스에서 재정의할 수 있는 상응하는 콜백 메서드가 있습니다.
* 수명 주기 메서드의 핵심 집합은 다음과 같습니다. onCreate()onStart()onPause()onRestart()onResume()onStop()onDestroy()
* 활동이 수명 주기 상태로 전환될 때 발생하는 동작을 추가하려면 상태의 콜백 메서드를 재정의합니다.
Android 스튜디오에서 스켈레톤 재정의 메서드를 클래스에 추가하려면 Code > Override Methods를 선택하거나 Control+o(Mac은 Command+o)를 누릅니다.

* 로그로 로깅
* Android Logging API와 특히 Log 클래스를 사용하여 Android 스튜디오 내 Logcat에 표시되는 짧은 메시지를 작성할 수 있습니다.
*Log.d()를 사용하여 디버그 메시지를 작성합니다.
*이 메서드는 두 가지 인수를 사용합니다. 로그 태그(일반적으로 클래스 이름)와 로그 메시지(짧은 문자열)입니다.
* Android 스튜디오에서 Logcat 창을 사용하여 작성한 메시지를 비롯한 시스템 로그를 확인합니다.

* 액티비티 상태 유지
* 앱이 백그라운드로 전환되면 onStop()이 호출된 직후 앱 데이터를 번들에 저장할 수 있습니다.
* EditText의 콘텐츠와 같은 일부 앱 데이터는 자동으로 저장됩니다.
* 번들은 키와 값의 모음인 Bundle의 인스턴스입니다. 키는 항상 문자열입니다.
* onSaveInstanceState() 콜백을 사용하여 앱이 자동으로 종료된 경우에도 유지하려는 번들에 기타 데이터를 저장합니다.
* 번들에 데이터를 넣으려면 put으로 시작하는 번들 메서드(예: putInt())를 사용합니다.
* onRestoreInstanceState() 메서드 또는 더 일반적인 onCreate()의 번들에서 데이터를 다시 가져올 수 있습니다.
* onCreate() 메서드에는 번들을 보유하는 savedInstanceState 매개변수가 있습니다.
* savedInstanceState 변수가 null이면 활동이 상태 번들 없이 시작되어 검색할 상태 데이터가 없습니다.
* 키를 사용하여 번들에서 데이터를 검색하려면 get으로 시작하는 Bundle 메서드(예: getInt())를 사용합니다

* 구성 변경
* 구성 변경은 기기 상태가 매우 급격하게 변경되어 시스템이 변경사항을 확인하는 가장 쉬운 방법이 활동을 소멸시키고 다시 빌드하는 것일 때 발생합니다.
* 구성 변경은 사용자가 기기를 세로 모드에서 가로 모드로 또는 가로 모드에서 세로 모드로 회전할 때 가장 흔하게 발생합니다.
* 기기 언어가 변경되거나 하드웨어 키보드가 연결될 때도 구성 변경이 발생할 수 있습니다.
* 구성 변경이 발생하면 Android는 모든 활동 수명 주기의 종료 콜백을 호출합니다. 그런 다음 Android는 처음부터 활동을 다시 시작하여 모든 수명 주기 시작 콜백을 실행합니다.
* Android는 구성 변경으로 인해 앱을 종료할 때 onCreate()에서 사용할 수 있는 상태 번들로 활동을 다시 시작합니다.
* 프로세스 종료와 마찬가지로 앱 상태를 onSaveInstanceState()의 번들에 저장합니다.*/

package com.example.android.dessertclicker

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import com.example.android.dessertclicker.databinding.ActivityMainBinding

    const val TAG = "MainActivity"
    const val KEY_REVENUE = "revenue_key"
    const val KEY_DESSERT_SOLD = "dessert_sold_key"

class MainActivity : AppCompatActivity() {


    private var revenue = 0
    private var dessertsSold = 0

    // Contains all the views
    private lateinit var binding: ActivityMainBinding

    /** Dessert Data **/

    /**
     * Simple data class that represents a dessert. Includes the resource id integer associated with
     * the image, the price it's sold for, and the startProductionAmount, which determines when
     * the dessert starts to be produced.
     */
    data class Dessert(val imageId: Int, val price: Int, val startProductionAmount: Int)

    // Create a list of all desserts, in order of when they start being produced
    private val allDesserts = listOf(
            Dessert(R.drawable.cupcake, 5, 0),
            Dessert(R.drawable.donut, 10, 5),
            Dessert(R.drawable.eclair, 15, 20),
            Dessert(R.drawable.froyo, 30, 50),
            Dessert(R.drawable.gingerbread, 50, 100),
            Dessert(R.drawable.honeycomb, 100, 200),
            Dessert(R.drawable.icecreamsandwich, 500, 500),
            Dessert(R.drawable.jellybean, 1000, 1000),
            Dessert(R.drawable.kitkat, 2000, 2000),
            Dessert(R.drawable.lollipop, 3000, 4000),
            Dessert(R.drawable.marshmallow, 4000, 8000),
            Dessert(R.drawable.nougat, 5000, 16000),
            Dessert(R.drawable.oreo, 6000, 20000)
    )

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }

    // 프로세스 종료와 마찬가지로 앱 상태를 번들에 저장함
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG,"onSaveInstanceState Called")

        /*putInt() 메서드 사용*/
        outState.putInt(KEY_REVENUE, revenue)
        outState.putInt(KEY_DESSERT_SOLD, dessertsSold)
    }

    private var currentDessert = allDesserts[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use Data Binding to get reference to the views
        Log.d(TAG, "onCreate Called")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        /*번들에 null이 있는지 확인*/
        if (savedInstanceState != null) {

            /*getInt()메서드 사용용*/
           revenue = savedInstanceState.getInt(KEY_REVENUE, 0) // 수익 값
            dessertsSold = savedInstanceState.getInt(KEY_DESSERT_SOLD, 0) //
            showCurrentDessert()
        }

        binding.dessertButton.setOnClickListener {
            onDessertClicked()
        }

        // Set the TextViews to the right values
        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // Make sure the correct dessert is showing
        binding.dessertButton.setImageResource(currentDessert.imageId)
    }

    /**
     * Updates the score when the dessert is clicked. Possibly shows a new dessert.
     */
    private fun onDessertClicked() {

        // Update the score
        revenue += currentDessert.price
        dessertsSold++

        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // Show the next dessert
        showCurrentDessert()
    }

    /**
     * Determine which dessert to show.
     */
    private fun showCurrentDessert() {
        var newDessert = allDesserts[0]
        for (dessert in allDesserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                newDessert = dessert
            }
            // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
            // you'll start producing more expensive desserts as determined by startProductionAmount
            // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
            // than the amount sold.
            else break
        }

        // If the new dessert is actually different than the current dessert, update the image
        if (newDessert != currentDessert) {
            currentDessert = newDessert
            binding.dessertButton.setImageResource(newDessert.imageId)
        }
    }

    /**
     * Menu methods
     */
    private fun onShare() {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
                .setText(getString(R.string.share_text, dessertsSold, revenue))
                .setType("text/plain")
                .intent
        try {
            startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.sharing_not_available),
                    Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareMenuButton -> onShare()
        }
        return super.onOptionsItemSelected(item)
    }
}
