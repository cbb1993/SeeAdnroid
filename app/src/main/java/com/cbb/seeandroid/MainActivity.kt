package com.cbb.seeandroid

import android.Manifest
import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cbb.seeandroid.aop.AopTest1Activity
import com.cbb.seeandroid.db.DBTestActivity
import com.cbb.seeandroid.skin.SkinActivity
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Rationale
import com.yanzhenjie.permission.RequestExecutor


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()
    }



    private fun requestPermission() {
        AndPermission.with(this)
            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.READ_EXTERNAL_STORAGE)
            .rationale(Rationale { _: Context?, _: List<String?>?, executor: RequestExecutor ->
                // 此处可以选择显示提示弹窗
                executor.execute()
            }) // 用户给权限了
            .onGranted(Action { permissions: List<String?>? ->

            }) // 用户拒绝权限，包括不再显示权限弹窗也在此列
            .onDenied(Action { permissions: List<String?>? ->
                // 判断用户是不是不再显示权限弹窗了，若不再显示的话进入权限设置页
                if (AndPermission.hasAlwaysDeniedPermission(this@MainActivity, permissions!!)) {
                    // 打开权限设置页
                    AndPermission.permissionSetting(this@MainActivity).execute()
                }

            })
            .start()
    }

    fun aop(view: View) {
        startActivity(Intent(this@MainActivity,AopTest1Activity::class.java))
    }
    fun db(view: View) {
        startActivity(Intent(this@MainActivity,DBTestActivity::class.java))
    }
    fun skin(view: View) {
        startActivity(Intent(this@MainActivity,SkinActivity::class.java))
    }

}
