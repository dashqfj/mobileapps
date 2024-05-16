package ru.mirea.borodkinada.employeedb;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

import ru.mirea.borodkinada.employeedb.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding	=	ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppDatabase db = App.getInstance().getDatabase();
        EmployeeDao employeeDao = db.employeeDao();
        Employee employee = new Employee();
        employee.id = 1;
        employee.name = "Batman";
        employee.power = "Smart and Rich";
        // запись героя в базу
        employeeDao.insert(employee);
        // Загрузка всех героев
        List<Employee> employees = employeeDao.getAll();
        // Получение определенного героя с id = 1
        employee = employeeDao.getById(1);
        // Обновление полей объекта
        employee.power = "Superhero";
        employeeDao.update(employee);
    }
}