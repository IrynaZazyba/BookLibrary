-- phpMyAdmin SQL Dump
-- version 4.7.3
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1:3306
-- Время создания: Сен 01 2020 г., 12:42
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
  `name` varchar(45) NOT NULL,
  `book_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `author`
--

INSERT INTO `author` (`id`, `name`, `book_id`) VALUES
(1, ' Dale Carnegie', 4),
(2, 'Стивен Хокинг', 3),
(3, 'Юваль Харари', 2),
(4, 'Lee Kuan Yew ', 1),
(5, 'Анджей Сапковский', 5),
(6, 'Берт Бейтс ', 6),
(7, ' Кэти Сьерра', 6);

-- --------------------------------------------------------

--
-- Структура таблицы `book`
--

CREATE TABLE `book` (
  `id` int(11) NOT NULL,
  `title` varchar(65) NOT NULL,
  `publish_date` date NOT NULL,
  `page_count` varchar(45) NOT NULL,
  `isbn` varchar(45) NOT NULL,
  `description` varchar(350) DEFAULT NULL,
  `total_amount` int(11) NOT NULL,
  `cover` varchar(45) DEFAULT NULL,
  `in_stock` int(11) NOT NULL,
  `publisher_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `book`
--

INSERT INTO `book` (`id`, `title`, `publish_date`, `page_count`, `isbn`, `description`, `total_amount`, `cover`, `in_stock`, `publisher_id`) VALUES
(1, 'From Third World to First: The Singapore Story - 1965-2000', '2000-10-03', '752 ', '978-0060197766', 'Few gave tiny Singapore much chance of survival when it was granted independence in 1965. How is it, then, that today the former British colonial trading post is a thriving Asian metropolis with not only the world\'s number one airline, best airport, and busiest port of trade, but also the world\'s fourth–highest per capita real income?', 11, NULL, 7, 1),
(2, 'Sapiens. Краткая история человечества', '2016-12-01', '520', '978-5-905891-64-9', 'Юваль Харари показывает, как ход истории формировал человеческое общество и действительность вокруг него. Его книга прослеживает связь между событиями прошлого и проблемами современности и заставляет читателя пересмотреть все устоявшиеся представления об окружающем мире.', 15, NULL, 5, 2),
(3, 'Краткие ответы на большие вопросы\r\n', '2019-01-31', '256', '978-5-04-099443-4', 'Перед вами книга-сенсация, книга-завещание, последний труд всемирно известного физика Стивена Хокинга, в которой он подводит некий итог и высказывается по самым главным вопросам, волнующим всех.', 15, NULL, 7, 3),
(4, 'How to Win Friends & Influence People', '1998-10-01', '288', '978-0671027032', 'Dale Carnegie’s rock-solid, time-tested advice has carried countless people up the ladder of success in their business and personal lives. Achieve your maximum potential—a must-read for the twenty-first century with more than 15 million copies sold!', 11, NULL, 3, 4),
(5, 'Ведьмак. Меч Предназначения\r\n', '2015-08-25', '384', '978-5-17-074112-0', 'Ведьмак - это мастер меча и мэтр волшебства, ведущий непрерывную войну с кровожадными монстрами, которые угрожают покою сказочной страны. \"Ведьмак\" - это мир на острие меча, ошеломляющее действие, незабываемые ситуации, великолепные боевые сцены.', 25, NULL, 0, 5),
(6, 'Изучаем Java\r\n', '2015-08-25', '720', '978-5-699-54574-2', '\"Изучаем Java\" — это не просто книга. Она не только научит вас теории языка Java и объектно-ориентированного программирования, она сделает вас программистом. В ее основу положен уникальный метод обучения на практике. В отличие от классических учебников информация дается не в текстовом, а в визуальном представлении.', 15, NULL, 1, 3);

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
  `reader_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `borrow_list`
--

INSERT INTO `borrow_list` (`id`, `borrow_date`, `due_date`, `return_date`, `comment`, `book_id`, `reader_id`) VALUES
(1, '2020-08-25 00:00:00', '2020-09-25 00:00:00', NULL, NULL, 1, 1),
(2, '2020-08-27 00:00:00', '2020-10-27 00:00:00', NULL, NULL, 4, 1),
(3, '2020-07-20 00:00:00', '2020-08-20 00:00:00', '2020-08-19 00:00:00', NULL, 6, 1);

-- --------------------------------------------------------

--
-- Структура таблицы `genre`
--

CREATE TABLE `genre` (
  `id` int(11) NOT NULL,
  `genre` varchar(45) NOT NULL,
  `book_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `genre`
--

INSERT INTO `genre` (`id`, `genre`, `book_id`) VALUES
(1, 'Education', 6),
(2, 'Fantasy', 5),
(3, 'Science fiction', 5),
(4, 'Business', 1),
(5, 'Self-Help', 4),
(6, 'Leadership & Motivation', 4),
(7, 'Business Professional\'s Biographies', 1),
(8, 'Science', 3),
(9, 'History', 2);

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
(5, 'АСТ');

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
(1, 'Iryna Zazybo', 'zazybo1.17@gmail.com');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `author`
--
ALTER TABLE `author`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`),
  ADD KEY `fk_author_book1_idx` (`book_id`);

--
-- Индексы таблицы `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`),
  ADD KEY `fk_book_publisher1_idx` (`publisher_id`);

--
-- Индексы таблицы `borrow_list`
--
ALTER TABLE `borrow_list`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`),
  ADD KEY `fk_borrow_list_book1_idx` (`book_id`),
  ADD KEY `fk_borrow_list_reader1_idx` (`reader_id`);

--
-- Индексы таблицы `genre`
--
ALTER TABLE `genre`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`),
  ADD KEY `fk_genre_book1_idx` (`book_id`);

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
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_UNIQUE` (`id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `author`
--
ALTER TABLE `author`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT для таблицы `book`
--
ALTER TABLE `book`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT для таблицы `borrow_list`
--
ALTER TABLE `borrow_list`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT для таблицы `genre`
--
ALTER TABLE `genre`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT для таблицы `publisher`
--
ALTER TABLE `publisher`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT для таблицы `reader`
--
ALTER TABLE `reader`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `author`
--
ALTER TABLE `author`
  ADD CONSTRAINT `fk_author_book1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `fk_book_publisher1` FOREIGN KEY (`publisher_id`) REFERENCES `publisher` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `borrow_list`
--
ALTER TABLE `borrow_list`
  ADD CONSTRAINT `fk_borrow_list_book1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_borrow_list_reader1` FOREIGN KEY (`reader_id`) REFERENCES `reader` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `genre`
--
ALTER TABLE `genre`
  ADD CONSTRAINT `fk_genre_book1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
