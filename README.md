# 💬 TCP Multithreaded Chat

## 1️⃣ Description
A simple **two-way TCP chat application** built with Java sockets and multithreading.

Both the **server and the client** can send and receive text messages independently.
Communication is handled using two parallel threads per endpoint:
- **Sender Thread** – outgoing messages,
- **Receiver Thread** – incoming messages.

The project demonstrates basic **multi-threaded TCP client–server communication**
using `Socket`, `ServerSocket`, `Thread`, and `Runnable`, enabling full-duplex I/O.

---

## 🧩 Compilation
```bash
javac -d out src/chat_tcp/*.java
```

## ▶️ Run
##### Terminal 1 – start the server
```bash
java -cp out chat_tcp.ChatServerTCP
```
The server listens on port: 7777

#### Terminal 2 – start the client
```bash
java -cp out chat_tcp.ChatClientTCP
```
You may also connect to a remote host:
```bash
java -cp out chat_tcp.ChatClientTCP 192.168.0.10
```

## 💡 Usage
- Type any text and press ENTER to send a message.
- Messages appear instantly on both terminals.
- Either side may initiate sending at any time.
- Type the following command to exit the application:
```bash
  /quit
```


# 📡 UDP Multithreaded Chat

## 1️⃣ Description
A **two-way UDP chat application** implemented with datagram sockets and multithreading.

Each endpoint uses two independent threads:
- **Sender Thread** for sending keyboard input,
- **Receiver Thread** for receiving UDP datagrams.

The project demonstrates **asynchronous, connectionless communication**
using `DatagramSocket` and `DatagramPacket` without persistent connections.

---

## 🧩 Compilation
```bash
javac -d out src/chat_udp/*.java
```

## ▶️ Run
#### Terminal 1 – start the server
```bash
java -cp out chat_udp.ChatServerUDP
```
Server listens on port: 7777

#### Terminal 2 – start the client
```bash
java -cp out chat_udp.ChatClientUDP
```
To connect to a remote server:
```bash
java -cp out chat_udp.ChatClientUDP 192.168.0.10
```

## 💡 Usage
- Both sides may type and send messages at any time.
- Incoming messages are displayed instantly.
- There is no guaranteed packet order or delivery (UDP behavior).
- Type the following command to exit the application:
```bash
/quit
```

## 🔄 TCP vs UDP – Protocol Comparison

This project contains two implementations of a network chat:
- **TCP chat** – built on connection-oriented, reliable sockets.
- **UDP chat** – built on connectionless datagrams requiring manual session handling.

TCP automatically establishes and manages client sessions, guarantees message delivery, preserves message order, and detects disconnections.
UDP does **not** create a persistent connection – the application must implement handshake logic, manage client state (IP + port), and handle disconnect signaling manually.  
UDP offers lower latency at the cost of reliability and delivery guarantees.

---

### 📊 TCP vs UDP Comparison Table

| Feature | **TCP** | **UDP** |
|--------|---------|---------|
| Connection type | Connection-oriented | Connectionless |
| Handshake | Built-in (automatic) | Not provided – must be implemented manually |
| Session state | Maintained by protocol | Managed in application code |
| Delivery guarantee | ✅ Guaranteed | ❌ Not guaranteed |
| Message ordering | ✅ Preserved | ❌ Not guaranteed |
| Duplicate prevention | ✅ Yes | ❌ No |
| Packet loss | ❌ Prevented by retransmission | ✅ Possible |
| Flow & congestion control | ✅ Yes | ❌ No |
| Latency | Higher | Lower |
| Performance | Moderate | Very high |
| Implementation complexity | Low | Higher |
| Typical use cases | Chats, HTTP, FTP, email | Games, VoIP, streaming, live data |


### 🇵🇱 Polish version below ⬇️

# 💬 TCP Multithreaded Chat

## 1️⃣ Opis
Prosta aplikacja **czatu TCP w trybie dwukierunkowym**, zbudowana w Javie z użyciem gniazd sieciowych i wielowątkowości.

Zarówno **serwer, jak i klient** mogą niezależnie wysyłać oraz odbierać wiadomości tekstowe.
Komunikacja realizowana jest przez **dwa równoległe wątki** po każdej stronie:
- **Wątek nadawczy (Sender Thread)** – obsługa wiadomości wychodzących,
- **Wątek odbiorczy (Receiver Thread)** – odbiór wiadomości przychodzących.

Projekt demonstruje podstawową **wielowątkową komunikację klient–serwer w TCP**  
z użyciem `Socket`, `ServerSocket`, `Thread` oraz `Runnable`, umożliwiając pełny duplex I/O.

---

## 🧩 Kompilacja
```bash
javac -d out src/chat_tcp/*.java
```

## ▶️ Uruchamianie
##### Terminal 1 – uruchomienie serwera
```bash
java -cp out chat_tcp.ChatServerTCP
```
Serwer nasłuchuje na porcie: 7777

#### Terminal 2 – uruchomienie klienta
```bash
java -cp out chat_tcp.ChatClientTCP
```
Połączenie z hostem zdalnym:
```bash
java -cp out chat_tcp.ChatClientTCP 192.168.0.10
```


## 💡 Użycie
- Wpisz dowolny tekst i naciśnij ENTER, aby wysłać wiadomość.
- Wiadomości pojawiają się natychmiast w obu terminalach.
- Każda strona może wysyłać wiadomości w dowolnym momencie.
- Aby zamknąć aplikację, użyj polecenia:
```bash
  /quit
```


# 📡 UDP Multithreaded Chat – Wersja Polska

## 1️⃣ Opis
Aplikacja **czatu UDP w trybie dwukierunkowym** oparta na gniazdach datagramowych
i wielowątkowej obsłudze komunikacji.

Każdy punkt końcowy wykorzystuje dwa niezależne wątki:
- **Wątek nadawczy (Sender Thread)** – wysyłanie danych z klawiatury,
- **Wątek odbiorczy (Receiver Thread)** – odbiór pakietów UDP.

Projekt demonstruje **asynchroniczną, bezpołączeniową komunikację**
z użyciem `DatagramSocket` oraz `DatagramPacket`, bez utrzymywania stałego połączenia.

---

## 🧩 Kompilacja
```bash
javac -d out src/chat_udp/*.java
```

## ▶️ Uruchamianie
#### Terminal 1 – uruchomienie serwera
```bash
java -cp out chat_udp.ChatServerUDP
```
Serwer nasłuchuje na porcie: 7777

#### Terminal 2 – uruchomienie klienta
```bash
java -cp out chat_udp.ChatClientUDP
```
Połączenie z serwerem zdalnym:
```bash
java -cp out chat_udp.ChatClientUDP 192.168.0.10
```

## 💡 Użycie
- Obie strony mogą wysyłać wiadomości w dowolnym momencie.
- Odebrane wiadomości są wyświetlane natychmiast.
- Kolejność dostarczania pakietów ani ich skuteczne doręczenie nie są gwarantowane (cecha UDP).
- Aby wyjść z aplikacji, użyj polecenia:
```bash
  /quit
```

## 🔄 TCP vs UDP – Porównanie protokołów

Projekt zawiera dwie implementacje komunikatora sieciowego:
- **Czat TCP** – oparty na połączeniowych, niezawodnych gniazdach.
- **Czat UDP** – wykorzystujący bezpołączeniowe datagramy, wymagające ręcznej obsługi sesji.

TCP automatycznie zestawia połączenie, zarządza sesją klienta, gwarantuje dostarczenie danych,
zachowuje kolejność wiadomości oraz wykrywa rozłączenia.  
UDP **nie** utrzymuje trwałego połączenia – aplikacja musi samodzielnie realizować handshake,
zarządzanie adresem klienta (IP + port) oraz sygnalizowanie rozłączeń.  
Zaletą UDP jest **niższe opóźnienie transmisji**, kosztem braku gwarancji niezawodności.

---

### 📊 Tabela porównawcza TCP vs UDP

| Cecha | **TCP** | **UDP** |
|------|---------|---------|
| Typ połączenia | Połączeniowy | Bezpołączeniowy |
| Handshake | Wbudowany (automatyczny) | Brak – wymagany mechanizm w aplikacji |
| Stan sesji | Zarządzany przez protokół | Zarządzany w kodzie aplikacji |
| Gwarancja dostarczenia | ✅ Tak | ❌ Nie |
| Zachowanie kolejności | ✅ Tak | ❌ Nie |
| Eliminacja duplikatów | ✅ Tak | ❌ Nie |
| Utrata pakietów | ❌ Zapobiegana retransmisją | ✅ Możliwa |
| Kontrola przepływu i przeciążenia | ✅ Tak | ❌ Nie |
| Opóźnienia | Wyższe | Niższe |
| Wydajność | Średnia | Bardzo wysoka |
| Złożoność implementacji | Niska | Wyższa |
| Przykładowe zastosowania | Czaty, HTTP, FTP, e-mail | Gry, VoIP, streaming, transmisje na żywo |

