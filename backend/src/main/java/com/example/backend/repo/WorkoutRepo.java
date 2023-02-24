package com.example.backend.repo;

import com.example.backend.service.IdGenerator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.example.backend.model.Workout;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WorkoutRepo {

    private final List<Workout> workoutList = new ArrayList<>();

    public List<Workout> listAllWorkouts() {
        return workoutList;
    }


    public Workout addWorkout(Workout workout) {
        workoutList.add(workout);
        return workout;

    }

}