package com.example.matrixsystem.spring_data.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class Initializer {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public Initializer(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @PostConstruct
    public void init() {
        jdbcTemplate.execute("insert into module(name) values ('Планиметрия');");
        jdbcTemplate.execute("insert into module(name) values ('Векторы');");
        jdbcTemplate.execute("insert into module(name) values ('Стереометрия');");
        jdbcTemplate.execute("insert into module(name) values ('Начала теории вероятностей');");
        jdbcTemplate.execute("insert into module(name) values ('Вероятности сложных событий');");
        jdbcTemplate.execute("insert into module(name) values ('Простейшие уравнения');");
        jdbcTemplate.execute("insert into module(name) values ('Вычисления и преобразования');");
        jdbcTemplate.execute("insert into module(name) values ('Производная и первообразная');");
        jdbcTemplate.execute("insert into module(name) values ('Прикладные задачи');");
        jdbcTemplate.execute("insert into module(name) values ('Текстовые задачи');");
        jdbcTemplate.execute("insert into module(name) values ('Графики функций');");
        jdbcTemplate.execute("insert into module(name) values ('Максимум и минимум функции');");
        jdbcTemplate.execute("insert into module(name, part, max_points, verifiable) values ('Уравнения', 'SECOND', 3, false);");
        jdbcTemplate.execute("insert into module(name, part, max_points, verifiable) values ('Стереометрическая задача', 'SECOND', 3, false);");
        jdbcTemplate.execute("insert into module(name, part, max_points, verifiable) values ('Неравенства', 'SECOND', 3, false);");
        jdbcTemplate.execute("insert into module(name, part, max_points, verifiable) values ('Финансовая математика', 'SECOND', 3, false);");
        jdbcTemplate.execute("insert into module(name, part, max_points, verifiable) values ('Планиметрическая задача', 'SECOND', 3, false);");
        jdbcTemplate.execute("insert into module(name, part, max_points, verifiable) values ('Задача с параметром', 'SECOND', 3, false);");
        jdbcTemplate.execute("insert into module(name, part, max_points, verifiable) values ('Числа и их свойства', 'SECOND', 3, false);");

        jdbcTemplate.execute("insert into users(login, password, role) values ('god','god','GOD');");
        jdbcTemplate.execute("insert into users(login, password, role) values ('teacher','teacher','TEACHER');");
        jdbcTemplate.execute("insert into users(login, password, role) values ('student','student','STUDENT');");
        jdbcTemplate.execute("insert into users(login, password, role) values ('manager','manager','MANAGER');");

        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '2', 1);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '212', 2);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '2', 3);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '212', 4);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '2', 5);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '212', 6);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '2', 7);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '212', 8);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '2', 9);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '212', 10);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '2', 11);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '212', 12);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '2', 13);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '212', 14);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '2', 15);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '212', 16);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '212', 14);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '2', 15);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '212', 16);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '212', 17);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '2', 18);");
        jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '212', 19);");

        jdbcTemplate.execute("insert into section(module_id, name, link) values (1, 'Конспект теории', 'https://miro.com/app/board/uXjVNuEFRQU=/?share_link_id=347896643047');");
        jdbcTemplate.execute("insert into section(module_id, name, link) values (1, 'Решение практических задач', 'https://miro.com/app/board/uXjVNuEFRQU=/?share_link_id=347896643047');");

        jdbcTemplate.execute("insert into section(module_id, name, link) values (2, 'Конспект теории', 'https://miro.com/app/board/uXjVNuEFRQU=/?share_link_id=347896643047');");
        jdbcTemplate.execute("insert into section(module_id, name, link) values (2, 'Решение практических задач', 'https://miro.com/app/board/uXjVNuEFRQU=/?share_link_id=347896643047');");
    }
}