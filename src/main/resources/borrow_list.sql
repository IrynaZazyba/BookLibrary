-- phpMyAdmin SQL Dump
-- version 4.7.3
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1:3306
-- Время создания: Сен 17 2020 г., 18:37
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
(27, '2020-09-01 00:00:00', '2020-10-01 00:00:00', '2020-09-17 16:23:14', 'Comment\n                ', 8, 3, 2),
(28, '2020-09-01 00:00:00', '2020-10-01 00:00:00', '2020-09-17 00:00:00', NULL, 8, 1, 1),
(29, '2020-09-01 00:00:00', '2020-12-01 00:00:00', NULL, NULL, 5, 2, NULL),
(30, '2020-09-17 14:59:50', '2021-03-17 14:59:50', '2020-09-17 16:11:33', 'Comment\n                ', 8, 4, 2),
(34, '2020-09-17 17:02:07', '2021-03-17 17:02:07', '2020-09-17 17:10:05', 'Comment\n                ', 10, 1, 3);

--
-- Индексы сохранённых таблиц
--

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
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `borrow_list`
--
ALTER TABLE `borrow_list`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;
--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `borrow_list`
--
ALTER TABLE `borrow_list`
  ADD CONSTRAINT `borrow_list_ibfk_1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  ADD CONSTRAINT `fk_borrow_list_book1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_borrow_list_reader1` FOREIGN KEY (`reader_id`) REFERENCES `reader` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
