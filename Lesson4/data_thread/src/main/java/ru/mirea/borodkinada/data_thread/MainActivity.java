package ru.mirea.borodkinada.data_thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import ru.mirea.borodkinada.data_thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Runnable runn1 = new Runnable() {
            public void run() {
                binding.tvInfo.setText("runn1");
            }
        };
        final Runnable runn2 = new Runnable() {
            public void run() {
                binding.tvInfo.setText("runn2");
            }
        };
        final Runnable runn3 = new Runnable() {
            public void run() {
                binding.tvInfo.setText("runn3");
            }
        };
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    runOnUiThread(runn1);
                    TimeUnit.SECONDS.sleep(1);
                    binding.tvInfo.postDelayed(runn3, 2000);
                    binding.tvInfo.post(runn2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        String description = "runOnUiThread(Runnable): Этот метод используется для выполнения кода в основном UI потоке. " +
                "Когда этот метод вызывается из фонового потока, заданный Runnable будет добавлен в очередь сообщений " +
                "основного потока и выполнен, как только основной поток будет доступен.\n\n" +
                "post(Runnable): Метод post() используется для запуска Runnable на View, который будет выполняться " +
                "в UI потоке. Это происходит путем помещения сообщения в очередь. В отличие от runOnUiThread, " +
                "post() можно вызывать из любого места, и выполнение начнется в следующем цикле обработки сообщений UI потока.\n\n" +
                "postDelayed(Runnable, long): Этот метод похож на post(), но добавляет возможность запланировать выполнение " +
                "Runnable с задержкой. Runnable будет помещен в очередь и выполнен после указанного количества миллисекунд.\n\n" +
                "Последовательность вызовов:\n" +
                "1. runn1 выполняется первым через runOnUiThread() после задержки в 2 секунды, начиная выполнение немедленно после " +
                "просыпания потока.\n" +
                "2. runn2 выполняется вторым без задержки с помощью post(), следуя сразу за runn1.\n" +
                "3. runn3 выполняется последним через postDelayed() с дополнительной задержкой в 2 секунды после runn2.";

        binding.tvInfo.setText(description);

    }
}