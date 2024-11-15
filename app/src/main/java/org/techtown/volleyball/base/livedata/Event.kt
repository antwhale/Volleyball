package org.techtown.volleyball.base.livedata

class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if(hasBeenHandled) { //이벤트가 이미 처리되었다면
            null
        } else {   //그렇지 않다면
            hasBeenHandled = true   //이벤트가 처리되었다고 표시한 후
            content     //값을 반환
        }
    }

    //이벤트 처리 여부와 상관없이 값을 반환
    fun peekContent(): T = content
}