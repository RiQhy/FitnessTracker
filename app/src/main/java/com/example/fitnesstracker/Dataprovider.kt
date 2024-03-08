package com.example.fitnesstracker

object Dataprovider{
    val programs: MutableList<Program> = java.util.ArrayList()

    init {
        programs.add(Program("Program_Arms"))
        programs.add(Program("Program_Uperbody"))
        programs.add(Program("Program_Leggs"))
    }
}
