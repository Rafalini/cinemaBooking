package com.cinema;

import com.cinema.integrationTests.MovieControllerTest;
import com.cinema.integrationTests.ReservationControllerTest;
import com.cinema.integrationTests.RoomControllerTest;
import com.cinema.integrationTests.ScreeningControllerTest;
import org.junit.jupiter.api.ClassDescriptor;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;

import java.util.Comparator;

public class SpringBootTestClassOrderer implements ClassOrderer {
    @Override
    public void orderClasses(ClassOrdererContext classOrdererContext) {
        classOrdererContext.getClassDescriptors().sort(Comparator.comparingInt(SpringBootTestClassOrderer::getOrder));
    }

    private static int getOrder(ClassDescriptor classDescriptor) {
        if (classDescriptor.getTestClass() == ReservationControllerTest.class) {
            return 5;
        } else if (classDescriptor.getTestClass() == ScreeningControllerTest.class) {
            return 4;
        } else if (classDescriptor.getTestClass() == RoomControllerTest.class) {
            return 3;
        } else if (classDescriptor.getTestClass() == MovieControllerTest.class) {
            return 2;
        } else {
            return 1;
        }
    }
}
