## Сущности
### Bank
при инициализации указываются допустимые валюты, запускаются потоки для обновления валют, у банка определены методы работы с транзакциями. В зависимости от события, в очерель добовляются объекты унаследованные от класса Transaction с переопределенной под свою роль функцией start.
### Client
логика обработки баланса абстрактна (ее нет), amount и currancy - два вида баланса, при событии указывается какая валюта будет у этой суммы, и в зависимости от этого осуществляется перерасчет
Для синхронизаций используется synchronized на объекте, в котором проиходят изменения
### Cashier
Обрабатывает очередь транзакций
### Transaction
При успешной обработке в logger поступит сообщение об успешном окончании, при ошибке (недостаточно средств), сообщение об ошибке.
### ErrorApi
Кастом класс для Error
### Logger
для вывода в консоль создана отдельная очередь (все логи выводятся туда), которую с момента инициализации слушает отдельный поток, и поочереди выводит сообщения.

### В Main указываются начальные параметры:
*количество клиентов
*подписки на оповещения у каждого клиента
*начальный баланс у клиентов
*паузы 
*запускается слушатель для вывода логов
