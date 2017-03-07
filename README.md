# EdtSmsCode 验证码控件

#xml：

<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.wcong.edtsmscode.EdtSmsCodeLayout
        android:id="@+id/edt"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        app:item_size="40dp"
        app:max_len="6"
        app:text_color="@color/colorPrimary"
        app:text_size="16dp" />
</RelativeLayout>

#Activity：

        ((EdtSmsCodeLayout) findViewById(R.id.edt)).setInputFinishListener(new EdtSmsCodeLayout.InputFinishListener() {
            @Override
            public void onInputFinish(String code) {
                Toast.makeText(MainActivity.this, code, Toast.LENGTH_SHORT).show();
            }
        });
       
        
#控件属性：
app:item_size="40dp"  每个Edittext的长宽，默认值40
app:max_len="6"  Edittext个数，默认为6
app:text_size="16dp"  文字大小，默认为16
app:text_color="@color/colorPrimary"  文字颜色，默认为#333333 黑
