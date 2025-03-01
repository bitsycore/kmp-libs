package sh.bitsy.test

import logFrame

fun secondShow() {
    logFrame()
}

class Hello {
    fun show() {
        destroy()
    }

    fun destroy() {
        abc(50, null, null)
    }

    fun abc (a: Int, hello: Hello?, world: Hello?) {
        if (hello == null && world != null) println("\\/")
        logFrame()
    }

    fun getAThrowable() = Throwable()
}