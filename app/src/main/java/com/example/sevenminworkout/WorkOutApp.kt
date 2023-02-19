package com.example.sevenminworkout

import android.app.Application

class WorkOutApp: Application() {
    // kreiramo db vrijednost kao lazy - unosi se vrijednost u varijablu kad nam treba, a ne direktno
    val db by lazy {
        // db ce biti instance od HistoryDatabase (samo jedan instance moze -zato sto je fun u companion objectu - singleton)
        // dobijamo instancu kad unosimo HistoryDatabase-this kao Application context
        HistoryDatabase.getInstance(this)
    }

}