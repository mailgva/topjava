package ru.javawebinar.topjava.util;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;
import java.util.StringJoiner;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void checkValidEmail(UserService service, User bean) {
        List<User> users = service.getAll();
        User user = null;
        for(User u : users)
            if(u.getEmail().equalsIgnoreCase(bean.getEmail())) {
                user = u;
                break;
            }

        if (user != null && (bean.getId() != user.getId())) {
            throw new DataIntegrityViolationException("User with this email already exists: " + bean.getEmail());
        }
    }


    public static void checkMealDateTime(MealService service, Meal meal, int id) {
        List<Meal> meals = service.getBetweenDateTimes(meal.getDateTime(), meal.getDateTime(), id);
        if (meals.size() > 0) {
            if(meals.get(0).getId() != meal.getId())
                throw new DataIntegrityViolationException("Meal with this datetime already exists: " + meal.getDateTime());
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      http://stackoverflow.com/a/32728226/548473
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }



    public static ResponseEntity<String> getErrorResponse(BindingResult result) {

        return new ResponseEntity<>(getErrorMessages(result), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public static String getErrorMessages(BindingResult result) {
        StringJoiner joiner = new StringJoiner("<br>");
        result.getFieldErrors().forEach(
                fe -> {
                    String msg = fe.getDefaultMessage();
                    if (!msg.startsWith(fe.getField())) {
                        msg = fe.getField() + ' ' + msg;
                    }
                    joiner.add(msg);
                });
        return joiner.toString();
    }

}