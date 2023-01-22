package in.reqres;

import in.reqres.data.model.*;
import in.reqres.spec.Specifications;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static in.reqres.config.ProjectConfig.PROPS;
import static in.reqres.data.Data.*;
import static in.reqres.data.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;


public class TestReqres {

    @Test
    @DisplayName("Успешная регистрация")
    public void successRegTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        RegistrData user = new RegistrData(PROPS.getEmail(), PROPS.getPassword());
        SuccessRegData successReg = given()
                .body(user)
                .when()
                .post(API_REGISTER)
                .then().log().all()
                .extract().as(SuccessRegData.class);

        Assertions.assertNotNull(successReg.getId());
        Assertions.assertNotNull(successReg.getToken());

        Assertions.assertEquals(ID, successReg.getId());
        Assertions.assertEquals(PROPS.getToken(), successReg.getToken());
    }

    @Test
    @DisplayName("Регистрация без пароля")
    public void unSuccessRegTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecError400());
        RegistrData user = new RegistrData(USER_FOR_UN_SUCCESS_REG, PASS_FOR_UN_SUCCESS_REG);
        UnSuccessRegData unSuccessRegData = given()
                .body(user)
                .post(API_REGISTER)
                .then().log().all()
                .extract().as(UnSuccessRegData.class);
        Assertions.assertEquals("Missing password", unSuccessRegData.getError());
    }

    @Test
    @DisplayName("Проверка ответа на наличие сортировки по годам")
    public void checkSortedYearsTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        List<ColorsData> colorsData = given()
                .when()
                .get(API_UNKNOWN)
                .then().log().all()
                .extract().body().jsonPath().getList("data", ColorsData.class);
        List<Integer> years = colorsData.stream().map(ColorsData::getYear).collect(Collectors.toList()); //Получаем список
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList()); // сортируем список для проверки
        Assertions.assertEquals(sortedYears, years);
        System.out.println(years);
        System.out.println(sortedYears);
    }

    @Test
    @DisplayName("Обновление пользователя")
    public void updateUser() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        UpdateUserInfo updateUserInfo = new UpdateUserInfo(USER_FOR_UPDATE_NAME, USER_FOR_UPDATE_JOB);
        UpdatedUserResponse updatedUserResponse = given()
                .body(updateUserInfo).log().all()
                .when()
                .put(API_USERS + 2)
                .then().log().all()
                .extract().as(UpdatedUserResponse.class);
        Assertions.assertEquals(updateUserInfo.getName(), updatedUserResponse.getName());
        Assertions.assertEquals(updateUserInfo.getJob(), updatedUserResponse.getJob());
        Assertions.assertNotNull(updatedUserResponse.getUpdatedAt());
    }

    @Test
    @DisplayName("Проверка статус кода после удаления")
    public void deleteUserTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUnique(204));
        given()
                .when()
                .delete(API_USERS + USER_FOR_DELETE)
                .then().log().all();
    }

    @Test
    @DisplayName("Проверка email пользователей на содержание reqres.in и проверка наличия avatar")
    public void checkAvatarAndIdTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        List<UserData> users = given()
                .when()
                .get(API_LIST_USERS)
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
        users.forEach(x -> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));
        Assertions.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));
    }

    @Test
    @DisplayName("Проверка email при помощи groovy")
    public void checEmailTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        given()
                .when()
                .get(API_USERS)
                .then().log().all()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("emma.wong@reqres.in"));
    }

}
