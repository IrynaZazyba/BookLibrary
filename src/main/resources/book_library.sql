-- phpMyAdmin SQL Dump
-- version 4.7.3
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1:3306
-- Время создания: Сен 21 2020 г., 23:49
-- Версия сервера: 10.2.7-MariaDB
-- Версия PHP: 7.1.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `book_library`
--

-- --------------------------------------------------------

--
-- Структура таблицы `author`
--

CREATE TABLE `author` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `author`
--

INSERT INTO `author` (`id`, `name`) VALUES
(1, ' Dale Carnegie'),
(2, 'Стивен Хокинг'),
(3, 'Юваль Харари'),
(4, 'Lee Kuan Yew '),
(5, 'Анджей Сапковский'),
(6, 'Берт Бейтс '),
(7, 'Кэти Сьерра'),
(8, 'Стив МакКоннелл'),
(9, 'Светлана Исакова'),
(10, 'Дмитрий Жемеров'),
(17, 'Т. Прутовых'),
(18, 'Джаннетт Уоллс'),
(19, 'Хелен Расселл'),
(20, 'Виктор Гюго'),
(21, 'Михаил Булгаков'),
(22, 'Владимир Набоков'),
(23, 'Илья Ильф'),
(24, 'Евгений Петров'),
(25, 'Вики Кинг'),
(26, 'Джоан Роулинг'),
(27, 'Диана Джонс'),
(28, 'Агата Кристи'),
(29, 'Дэн Браун');

-- --------------------------------------------------------

--
-- Структура таблицы `book`
--

CREATE TABLE `book` (
  `id` int(11) NOT NULL,
  `title` varchar(65) NOT NULL,
  `publish_date` date NOT NULL,
  `page_count` int(45) NOT NULL,
  `ISBN` varchar(45) NOT NULL,
  `description` varchar(350) DEFAULT NULL,
  `total_amount` int(11) NOT NULL,
  `cover` varchar(45) DEFAULT NULL,
  `in_stock` int(11) NOT NULL,
  `publisher_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `book`
--

INSERT INTO `book` (`id`, `title`, `publish_date`, `page_count`, `ISBN`, `description`, `total_amount`, `cover`, `in_stock`, `publisher_id`) VALUES
(1, 'From Third World to First: The Singapore Story - 1965-2000', '2000-10-03', 752, '978-0060197766', 'Few gave tiny Singapore much chance of survival when it was granted independence in 1965. How is it, then, that today the former British colonial trading post is a thriving Asian metropolis with not only the world\'s number one airline, best airport, and busiest port of trade, but also the world\'s fourth–highest per capita real income?', 11, NULL, 11, 1),
(2, 'Sapiens. Краткая история человечества', '2016-12-01', 520, '978-5-905891-64-9', 'Юваль Харари показывает, как ход истории формировал человеческое общество и действительность вокруг него. Его книга прослеживает связь между событиями прошлого и проблемами современности и заставляет читателя пересмотреть все устоявшиеся представления об окружающем мире.&quot;', 14, NULL, 13, 2),
(3, 'Краткие ответы на большие вопросы', '2019-01-31', 256, '978-5-04-099443-4', 'Перед вами книга-сенсация, книга-завещание, последний труд всемирно известного физика Стивена Хокинга, в которой он подводит некий итог и высказывается по самым главным вопросам, волнующим всех.&quot;', 15, NULL, 15, 3),
(4, 'How to Win Friends & Influence People', '1998-10-01', 288, '978-0671027032', 'Dale Carnegie’s rock-solid, time-tested advice has carried countless people up the ladder of success in their business and personal lives. Achieve your maximum potential—a must-read for the twenty-first century with more than 15 million copies sold!', 11, NULL, 11, 4),
(5, 'Ведьмак. Меч Предназначения', '2015-08-25', 384, '978-5-17-074112-0', 'Ведьмак - это мастер меча и мэтр волшебства, ведущий непрерывную войну с кровожадными монстрами, которые угрожают покою сказочной страны. &quot;Ведьмак&quot; - это мир на острие меча, ошеломляющее действие, незабываемые ситуации, великолепные боевые сцены.&quot;&quot;&quot;', 24, '5.jpg', 24, 5),
(6, 'Изучаем Java', '2015-08-25', 720, '978-5-699-54574-2', 'Изучаем Java&quot; &mdash; это не просто книга. Она не только научит вас теории языка Java и объектно-ориентированного программирования, она сделает вас программистом. В ее основу положен уникальный метод обучения на практике. В отличие от классических учебников информация дается не в текстовом, а в визуальном представлении.&quot;&quot;', 1, '6.png', 1, 3),
(8, 'Kotlin в действии', '2017-10-03', 402, '978-5-97060-497-7', 'Язык Kotlin предлагает выразительный синтаксис, мощную и понятную систему типов, великолепную поддержку и бесшовную совместимость с существующим кодом на Java, богатый выбор библиотек и фреймворков.&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;', 10, 'q.png', 10, 14),
(10, 'Совершенный код. Мастер-класс', '2019-10-03', 896, '978-5-9909805-1-8', 'Опираясь на академические исследования, с одной стороны, и практический опыт коммерческих разработок ПО - с другой, автор синтезировал из самых эффективных методик и наиболее эффективных принципов ясное прагматичное руководство.&quot;&quot;&quot;&quot;&quot;&quot;&quot;&quot;', 5, NULL, 5, 8),
(92, 'Теория Всего', '2016-12-27', 160, '978-5-17-102340-9', '&laquo;Теория всего&raquo; - это история Вселенной, рассказанная Стивеном Хокингом в привычной - прозрачной и остроумной - манере и дополненная фантастическими снимками космического телескопа &laquo;Хаббл&raquo;, от которых перехватывает дух.', 7, NULL, 7, 5),
(93, 'Польский за 30 дней', '2019-12-21', 192, '978-5-17-097185-5', '&quot;Польский за 30 дней&quot; - это простой и доступный курс польского языка. Самоучитель состоит из трех частей.', 25, NULL, 25, 5),
(94, 'Замок из стекла. Что скрывает прошлое', '2015-01-01', 416, '978-5-699-76178-4', 'В этой книге Уоллс рассказывает о своем детстве и взрослении в многодетной и необычной семье, в которой практиковались весьма шокирующие методы воспитания. Многие годы Джаннетт скрывала свое прошлое, пока не поняла, что, только освободившись от тайн и чувства стыда, она сможет принять себя и двигаться дальше.', 10, NULL, 10, 3),
(95, 'Хюгге, или Уютное счастье по-датски', '2017-01-01', 448, '978-5-699-95302-8', 'Променять Лондон и работу в Marie Claire на датский городок с населением 6 000 человек?! Сначала она твердо сказала &quot;НЕТ&quot;. Но потом... муж так мечтал поработать в LEGO.', 7, NULL, 7, 3),
(96, 'Собор Парижской Богоматери', '2012-01-01', 628, '978-5-389-04726-6', 'Виктор Гюго для французов прежде всего великий национальный поэт, реформатор стиха. В то же время он и создатель романтической драмы, великолепный прозаик, чьи произведения до сих пор увлекают читателей во всем мире.', 15, NULL, 15, 20),
(97, 'Мастер и Маргарита', '2012-01-01', 480, '978-5-389-01686-6', '&quot;Мастер и Маргарита&quot; М. А. Булгакова - самое удивительное и загадочное произведение ХХ века. Опубликованный в середине 1960-х, этот роман поразил читателей необычностью замысла, красочностью и фантастичностью действия, объединяющего героев разных эпох и культур.', 25, NULL, 25, 20),
(99, 'Лолита', '2013-01-01', 416, '978-5-389-08635-7', 'Одна из самых небанальных скандально известных историй оказалась на поверку историей о &quot;любви с первого взгляда, с последнего взгляда, с извечного взгляда&quot;.', 15, NULL, 15, 20),
(101, 'Двенадцать стульев', '2014-01-01', 384, '978-5-389-07219-0', 'Знаменитый роман-фельетон И.Ильфа и Е.Петрова &quot;Двенадцать стульев&quot; впервые был опубликован в 1928 году, а сегодня его называют в числе самых популярных произведений отечественной литературы XX века.', 17, NULL, 17, 20),
(102, 'Как написать кино за 21 день', '2018-01-01', 288, '978-5-00146-060-2', 'Хотите написать фильм? Вы обратились по адресу! Написанная легким и живым языком, эта книга проведет вас по самому короткому пути от вашей великолепной идеи до законченного сценария.', 5, '102.jpg', 5, 21),
(103, 'Гарри Поттер и философский камень', '2016-01-01', 432, '978-5-389-07435-4', 'Одиннадцатилетний мальчик-сирота Гарри Поттер живет в семье своей тетки и даже не подозревает, что он - настоящий волшебник. Но однажды прилетает сова с письмом для него, и жизнь Гарри Поттера изменяется навсегда.', 25, '103.jpg', 25, 22),
(104, 'Ходячий замок', '2011-01-01', 448, '978-5-389-02467-0', 'Софи живет в сказочной стране, где ведьмы и русалки, семимильные сапоги и говорящие собаки - обычное дело. Поэтому, когда на нее обрушивается ужасное проклятие коварной Болотной Ведьмы, Софи ничего не остается, как обратиться за помощью к таинственному чародею Хоулу, обитающему в Ходячем замке.', 19, '104.jpg', 19, 20),
(105, 'Убийство в &quot;Восточном экспрессе&quot;', '2019-01-01', 320, '978-5-04-098115-1', 'Находившийся в Стамбуле великий сыщик Эркюль Пуаро возвращается в Англию на знаменитом &quot;Восточном экспрессе&quot;, в котором вместе с ним едут, кажется, представители всех возможных национальностей.', 15, '105.jpg', 15, 3),
(106, 'Код да Винчи', '2016-01-01', 544, '978-5-17-086361-7', '&quot;Секретный код скрыт в работах Леонардо да Винчи...Только он поможет найти христианские святыни, дающие немыслимые власть и могущество... Клюк к величайшей тайне, над которой человечество билось веками, наконец может быть найден... &quot;', 17, '106.jpg', 17, 5);

-- --------------------------------------------------------

--
-- Структура таблицы `book_has_author`
--

CREATE TABLE `book_has_author` (
  `book_id` int(11) NOT NULL,
  `author_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `book_has_author`
--

INSERT INTO `book_has_author` (`book_id`, `author_id`) VALUES
(1, 4),
(2, 3),
(3, 2),
(4, 1),
(5, 5),
(6, 6),
(6, 7),
(8, 9),
(8, 10),
(10, 8),
(92, 2),
(93, 17),
(94, 18),
(95, 19),
(96, 20),
(97, 21),
(99, 22),
(101, 23),
(101, 24),
(102, 25),
(103, 26),
(104, 27),
(105, 28),
(106, 29);

-- --------------------------------------------------------

--
-- Структура таблицы `borrow_list`
--

CREATE TABLE `borrow_list` (
  `id` int(11) NOT NULL,
  `borrow_date` datetime NOT NULL,
  `due_date` datetime NOT NULL,
  `return_date` datetime DEFAULT NULL,
  `comment` varchar(150) DEFAULT NULL,
  `book_id` int(11) NOT NULL,
  `reader_id` int(11) NOT NULL,
  `status_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `borrow_list`
--

INSERT INTO `borrow_list` (`id`, `borrow_date`, `due_date`, `return_date`, `comment`, `book_id`, `reader_id`, `status_id`) VALUES
(45, '2020-09-21 23:20:03', '2021-09-21 23:20:03', '2020-09-21 23:36:45', 'Comment\n                ', 2, 11, 3),
(46, '2020-09-21 23:20:03', '2020-10-21 23:20:03', '2020-09-21 23:37:27', 'Comment\n                ', 2, 11, 1),
(47, '2020-09-21 23:20:48', '2020-12-21 23:20:48', NULL, 'Comment', 2, 3, NULL);

-- --------------------------------------------------------

--
-- Структура таблицы `genre`
--

CREATE TABLE `genre` (
  `id` int(11) NOT NULL,
  `genre` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `genre`
--

INSERT INTO `genre` (`id`, `genre`) VALUES
(1, 'Education'),
(2, 'Fantasy'),
(3, 'Science fiction'),
(4, 'Business'),
(5, 'Self-Help'),
(6, 'Leadership & Motivation'),
(7, 'Business Professional\'s Biographies'),
(8, 'Science'),
(9, 'History'),
(12, 'Образование'),
(13, 'Наука'),
(18, 'Eductation'),
(21, 'Право'),
(22, 'Боевик'),
(23, 'New Features'),
(27, 'Genre'),
(28, 'Нон-фикшн'),
(29, 'Саморазвитие'),
(30, 'Драма'),
(31, 'Стиль жизни'),
(32, 'Самообразование'),
(33, 'Исторический роман'),
(34, 'Роман'),
(35, 'Роман-фельетон'),
(36, 'Фэнтези'),
(37, 'Детектив');

-- --------------------------------------------------------

--
-- Структура таблицы `genre_has_book`
--

CREATE TABLE `genre_has_book` (
  `genre_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `genre_has_book`
--

INSERT INTO `genre_has_book` (`genre_id`, `book_id`) VALUES
(1, 6),
(2, 5),
(3, 5),
(4, 1),
(5, 4),
(6, 4),
(7, 1),
(8, 2),
(8, 3),
(8, 6),
(9, 2),
(12, 8),
(12, 10),
(12, 93),
(13, 8),
(13, 10),
(13, 92),
(18, 8),
(28, 92),
(29, 93),
(30, 94),
(31, 95),
(32, 95),
(32, 102),
(33, 96),
(34, 96),
(34, 97),
(34, 99),
(35, 101),
(36, 103),
(36, 104),
(37, 105),
(37, 106);

-- --------------------------------------------------------

--
-- Структура таблицы `publisher`
--

CREATE TABLE `publisher` (
  `id` int(11) NOT NULL,
  `publisher` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `publisher`
--

INSERT INTO `publisher` (`id`, `publisher`) VALUES
(1, 'Harper'),
(2, 'Синдбад'),
(3, 'Эксмо'),
(4, 'Pocket Books'),
(5, 'АСТ'),
(8, 'Русская редакция'),
(14, 'ДМК'),
(19, 'QWERTY'),
(20, 'Азбука'),
(21, 'Манн, Иванов и Фербер'),
(22, 'Махаон');

-- --------------------------------------------------------

--
-- Структура таблицы `reader`
--

CREATE TABLE `reader` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `reader`
--

INSERT INTO `reader` (`id`, `name`, `email`) VALUES
(1, 'Iryna Zazybo', 'lady.ira100@gmail.com'),
(2, 'Lorem', 'lorem_ipsum@gmail.com'),
(3, 'Ben Roberts', 'zazbenRoberts@gmail.com'),
(4, 'lorem', 'lorem@gmail.com'),
(10, 'zaz', 'zaz'),
(11, 'Iryna Zazybo', 'zazybo1.17@gmail.com'),
(12, 'iryna', 'zazaybo1.17@gmail.com');

-- --------------------------------------------------------

--
-- Структура таблицы `status`
--

CREATE TABLE `status` (
  `id` int(11) NOT NULL,
  `status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `status`
--

INSERT INTO `status` (`id`, `status`) VALUES
(1, 'RETURNED'),
(2, 'LOST'),
(3, 'RETURNED_AND_DAMAGED');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `author`
--
ALTER TABLE `author`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`);

--
-- Индексы таблицы `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`),
  ADD KEY `fk_book_publisher1_idx` (`publisher_id`);

--
-- Индексы таблицы `book_has_author`
--
ALTER TABLE `book_has_author`
  ADD PRIMARY KEY (`book_id`,`author_id`),
  ADD KEY `fk_book_has_author_author1_idx` (`author_id`),
  ADD KEY `fk_book_has_author_book1_idx` (`book_id`);

--
-- Индексы таблицы `borrow_list`
--
ALTER TABLE `borrow_list`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`),
  ADD KEY `fk_borrow_list_book1_idx` (`book_id`),
  ADD KEY `fk_borrow_list_reader1_idx` (`reader_id`),
  ADD KEY `status_id` (`status_id`);

--
-- Индексы таблицы `genre`
--
ALTER TABLE `genre`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`);

--
-- Индексы таблицы `genre_has_book`
--
ALTER TABLE `genre_has_book`
  ADD PRIMARY KEY (`genre_id`,`book_id`),
  ADD KEY `fk_genre_has_book_book1_idx` (`book_id`),
  ADD KEY `fk_genre_has_book_genre1_idx` (`genre_id`);

--
-- Индексы таблицы `publisher`
--
ALTER TABLE `publisher`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`);

--
-- Индексы таблицы `reader`
--
ALTER TABLE `reader`
  ADD UNIQUE KEY `id_UNIQUE` (`id`);

--
-- Индексы таблицы `status`
--
ALTER TABLE `status`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `author`
--
ALTER TABLE `author`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;
--
-- AUTO_INCREMENT для таблицы `book`
--
ALTER TABLE `book`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=111;
--
-- AUTO_INCREMENT для таблицы `borrow_list`
--
ALTER TABLE `borrow_list`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;
--
-- AUTO_INCREMENT для таблицы `genre`
--
ALTER TABLE `genre`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;
--
-- AUTO_INCREMENT для таблицы `publisher`
--
ALTER TABLE `publisher`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;
--
-- AUTO_INCREMENT для таблицы `reader`
--
ALTER TABLE `reader`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT для таблицы `status`
--
ALTER TABLE `status`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `fk_book_publisher1` FOREIGN KEY (`publisher_id`) REFERENCES `publisher` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `book_has_author`
--
ALTER TABLE `book_has_author`
  ADD CONSTRAINT `fk_book_has_author_author1` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_book_has_author_book1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `borrow_list`
--
ALTER TABLE `borrow_list`
  ADD CONSTRAINT `borrow_list_ibfk_1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  ADD CONSTRAINT `fk_borrow_list_book1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_borrow_list_reader1` FOREIGN KEY (`reader_id`) REFERENCES `reader` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `genre_has_book`
--
ALTER TABLE `genre_has_book`
  ADD CONSTRAINT `fk_genre_has_book_book1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_genre_has_book_genre1` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
