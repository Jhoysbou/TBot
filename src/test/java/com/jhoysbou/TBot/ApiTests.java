package com.jhoysbou.TBot;

import com.jhoysbou.TBot.models.Message;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class ApiTests {
    private final GroupApi api;

    @Autowired
    public ApiTests(GroupApi api) {this.api = api;}

    @Test
    void apiTest() throws IOException, InterruptedException {
        api.sendMessage(
                new Message("https://vk.com/photo-8812367_457241483\n" +
                        "✅Мы подготовили для тебя подробный алгоритм поступления в ВШТМ, в котором собрали ссылки на официальную и верную информацию:\n" +
                        "⬇\n" +
                        "1. Познакомься с ВШТМ  \uD83D\uDD0E vk.com/theormech и https://hstm.spbstu.ru/bachelor/ \n" +
                        "2. Пройди практику для старшеклассников (информация у нашего бота)\n" +
                        "3. Сдай ЕГЭ\uD83D\uDCDA\uD83D\uDCDD\n" +
                        "ЕГЭ нужно сдавать по трем предметам: \n" +
                        "- Математика профильная\n" +
                        "- русский язык\n" +
                        "- физика или информатика (на твой выбор)\n" +
                        "4. Проверь сроки приема \uD83D\uDCC5 https://www.spbstu.ru/abit/bachelor/oznakomitsya-with..\n" +
                        "5. Собери нужные документы \uD83D\uDCC1 https://www.spbstu.ru/abit/bachelor/apply/the-list-of..\n" +
                        "6. Подай заявление \uD83D\uDCC4 https://www.spbstu.ru/abit/bachelor/apply/methods-of-..\n" +
                        "7. Следи за списками подавших оригиналы \uD83D\uDCD1 https://www.spbstu.ru/abit/bachelor/ и https://vk.com/fmf_ipmm\n" +
                        "8. Подай согласие на зачисление в Личном кабинете абитуриента или очно в приемной комиссии СПбПУ\n" +
                        "9 Смотри приказ о зачислении\uD83D\uDC4F\uD83C\uDFFB✅ https://www.spbstu.ru/abit/bachelor/\n" +
                        "10. Ура!\uD83D\uDE03 Теперь ты - студент! Тебя ждет встреча с адаптерами и День Знаний!\n" +
                        "(смотри анонс в группе ВК https://vk.com/theormech\n" +
                        "Информационный сайт для первокурсников - https://www.spbstu.ru/freshman/ "),
                List.of(new Long[]{137239419L})
        );

    }
}
