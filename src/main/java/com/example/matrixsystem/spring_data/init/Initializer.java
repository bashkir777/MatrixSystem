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

        jdbcTemplate.execute("insert into users(login, password, role, miro_board) values ('god','god','GOD', 'uXjVNuEFRQU=');");
        jdbcTemplate.execute("insert into users(login, password, role, miro_board) values ('teacher','teacher','TEACHER', 'uXjVNuEFRQU=');");

        for(int i = 0; i < 20; i++){
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '1', 1);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '2', 2);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '3', 3);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '4', 4);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '5', 5);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '6', 6);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '7', 7);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '8', 8);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '8', 9);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '10', 10);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '11', 11);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '12', 12);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '13', 13);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '14', 14);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '15', 15);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '16', 16);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '17', 17);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABC', '18', 18);");
            jdbcTemplate.execute("insert into task(task, answer, module) values ('В треугольнике ABD Три стороны какая из них больше', '19', 19);");
        }


        jdbcTemplate.execute("insert into section(module_id, name, link) values (1, 'Конспект теории', 'https://miro.com/app/board/uXjVNuEFRQU=/?share_link_id=347896643047');");
        jdbcTemplate.execute("insert into section(module_id, name, link) values (1, 'Решение практических задач', 'https://miro.com/app/board/uXjVNuEFRQU=/?share_link_id=347896643047');");

        jdbcTemplate.execute("insert into section(module_id, name, link) values (2, 'Конспект теории', 'https://miro.com/app/board/uXjVNuEFRQU=/?share_link_id=347896643047');");
        jdbcTemplate.execute("insert into section(module_id, name, link) values (2, 'Решение практических задач', 'https://miro.com/app/board/uXjVNuEFRQU=/?share_link_id=347896643047');");

        jdbcTemplate.execute("insert into option() values ();");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 1);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 2);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 3);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 4);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 5);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 6);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 7);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 8);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 9);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 10);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 11);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 12);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 13);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 14);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 15);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 16);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 17);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 18);");
        jdbcTemplate.execute("insert into option_task(option_id, task_id) values (1, 19);");

        jdbcTemplate.execute("insert into custom_task(task, solution, answer) values ('Первое кастомное задание','Развернутое решение', '1');");
        jdbcTemplate.execute("insert into custom_task(task, solution, answer) values ('Второе кастомное задание','Развернутое решение', '1');");
        jdbcTemplate.execute("insert into custom_task(task, solution, answer) values ('Третье кастомное задание','Развернутое решение', '1');");
        jdbcTemplate.execute("insert into homework() values ();");
        jdbcTemplate.execute("insert into homework_custom_task(homework, custom_task) values (1, 1);");
        jdbcTemplate.execute("insert into homework_custom_task(homework, custom_task) values (1, 2);");
        jdbcTemplate.execute("insert into homework_custom_task(homework, custom_task) values (1, 3);");

        jdbcTemplate.execute("insert into user_custom_task(user_reference, task_reference, relation_type) values (1, 1, 'DONE');");
    }
}