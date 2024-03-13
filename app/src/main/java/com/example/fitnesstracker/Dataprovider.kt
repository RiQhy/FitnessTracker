package com.example.fitnesstracker

object Dataprovider{
    val programs: MutableList<Program> = java.util.ArrayList()
//programs shown on screen are here and then clicking thees will call API for corresponding Data.
    init {
        programs.add(Program("Program_Arms"))
        programs.add(Program("Program_Uperbody"))
        programs.add(Program("Program_LegDay"))
        programs.add(Program("Program_Cardio"))
        programs.add(Program("Program_HardCore"))
    }
}
