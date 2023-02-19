package com.example.sevenminworkout

object Constant {

    fun defaultExerciseList(): ArrayList<ExerciseModel> {
        val exerciseList = ArrayList<ExerciseModel>()
        val jumpingJacks = ExerciseModel(
            1,
            "Jumping Jacks",
            R.drawable.ic_jumping_jacks,
            false, // da li je zavrsena vjezba
            false // da li je izabrana vjezba
        )
        // dodavanje elemenata u listu
        exerciseList.add(jumpingJacks)

        val wallSit = ExerciseModel(
            2,
            "Čučanj uz zid",
            R.drawable.ic_wall_sit,
            false,
            false
        )
        exerciseList.add(wallSit)

        val pushUp = ExerciseModel(
            3,
            "Sklekovi",
            R.drawable.ic_push_up,
            false,
            false
        )
        exerciseList.add(pushUp)

        val abdominalCrunch = ExerciseModel(
            4,
            "Trbušnjaci",
            R.drawable.ic_abdominal_crunch,
            false,
            false
        )
        exerciseList.add(abdominalCrunch)

        val stepUpOnChair = ExerciseModel(
            5,
            "Penjanje na stolicu",
            R.drawable.ic_step_up_onto_chair,
            false,
            false
        )
        exerciseList.add(stepUpOnChair)

        val squat = ExerciseModel(
            6,
            "Čučnjevi",
            R.drawable.ic_squat,
            false,
            false
        )
        exerciseList.add(squat)

        val tricepDipOnChair = ExerciseModel(
            7,
            "Triceps propadanja",
            R.drawable.ic_triceps_dip_on_chair,
            false,
            false
        )
        exerciseList.add(tricepDipOnChair)

        val plank = ExerciseModel(
            8,
            "Daska",
            R.drawable.ic_plank,
            false,
            false
        )
        exerciseList.add(plank)

        val highKneesRunningInPlace = ExerciseModel(
            9,
            "Trčanje u mjestu",
            R.drawable.ic_high_knees_running_in_place,
            false,
            false
        )
        exerciseList.add(highKneesRunningInPlace)

        val lunges = ExerciseModel(
            10,
            "Iskorak",
            R.drawable.ic_lunge,
            false,
            false
        )
        exerciseList.add(lunges)

        val pushAndRotation = ExerciseModel(
            11,
            "Sklek i rotacija",
            R.drawable.ic_push_up_and_rotation,
            false,
            false
        )
        exerciseList.add(pushAndRotation)

        val sidePlank = ExerciseModel(
            12,
            "Bočna Daska",
            R.drawable.ic_side_plank,
            false,
            false

        )
        exerciseList.add(sidePlank)




        return exerciseList
    }
}