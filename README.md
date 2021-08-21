# <p align="center">AuthGate 1.0</p>
**EN:**

AuthGate - plugin for pirated minecraft servers. Its task is to register / authorize players
Version for servers on Spigot 1.12 - 1.17.1

**Commands:**

- /login [pass] - login command
- /register [pass] [pass] - command for registration (aliases - reg)

**Config (Default):**

- send_global_join_message = true - sending a global message about the player's entry
- send_local_join_message = false - sending a local message (to players within a radius) about the player's entry
- range_local_join_message = 40 - radius of sending a local message
- wrong_pass_count = 1 - the allowed number of errors at the entrance (if exceeded = kik)
- lang = en -plugin language (before edit, check your lenguage in lang.yml)
- enable_time_out = true - time out
- time_out = 60 - time out sec
- message_interval = 5 - the interval in sec for sending a message about a request to write /login or /reg
- send_title = true - title with text "/login [pass]" or "/reg [pass] [pass]"

____
**RU:**

AuthGate - плагин для приватских серверов майнкрафт для авторизации/регистрации игроков
Версия для серверов с ядром Spigot 1.12 - 1.17.1

**Команды:**

- /login [pass] - команда для входа
- /register [pass] [pass] - команда для регистрации (так же /reg)

**Конфиг (дефолтный):**

- send_global_join_message = true - отпарвка глобального сообщения о входе игрока
- send_local_join_message = false - отправка локального сообщения другим игрокам (в радиусе) при входе игрока
- range_local_join_message = 40 - rрадиус отправки локального сообщения
- wrong_pass_count = 1 - колличество допустимых неправильно введённых паролей (превышение - кик)
- lang = en - язык в плагине ( перед изменением языка, убедитесь что он есть в файле lang.yml)
- enable_time_out = true - использование тайм аута
- time_out = 60 - время для тайм аута ( по окончанию - кик) (в сек)
- message_interval = 5 -  интевал отправки сообщения о просьбе написать /login или /reg (в сек)
- send_title = true - отправлять ли тайтл с текстом "/login [pass]" или "/reg [pass] [pass]"


____
**[Download](https://github.com/2sweetheart2/AuthGate/releases)**

**[Скачать](https://github.com/2sweetheart2/AuthGate/releases)**

