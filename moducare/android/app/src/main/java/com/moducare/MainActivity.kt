package com.moducare

import android.os.Bundle
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultReactActivityDelegate
import org.devio.rn.splashscreen.SplashScreen

class MainActivity : ReactActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // SplashScreen.show()를 onCreate에서 호출
        SplashScreen.show(this)  
    }
    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    override fun getMainComponentName(): String = "moducare"

    /**
     * Returns the instance of the [ReactActivityDelegate].
     * DefaultReactActivityDelegate는 Fabric을 사용할 때 설정할 수 있습니다.
     * Fabric을 사용하지 않으므로 이 부분에서 `fabricEnabled`를 제거합니다.
     */
    override fun createReactActivityDelegate(): ReactActivityDelegate {
        return DefaultReactActivityDelegate(this, mainComponentName)
    }
}
