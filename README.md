# KOKODA - GRUJIC simple application for tracking egg orders in Clojure
This is a simple web application, created for the needs of my dear father, written in Clojure.

# Where did the initial idea come from?

After completing my undergraduate studies at the Faculty of Organizational Sciences, I decided to step out of my comfort zone and feel the challenge of programming on real problems, and thus acquire a new problem-solving skill.Therefore, my new learning journey began with the Master's program in Software Engineering and Artificial Intelligence.Is there a better way to learn programming than to encounter a new, a little bit exotic programming language. Of course, we're talking about Clojure, a dynamic and functional dialect of the Lisp programming language on the Java platform.
This months-long journey was not easy at all, it required a lot of surfing trought the Internet and searching for useful literature, good video material and examples of useful projects,but the most demanding of all was adapting to the logic of the language, which I have never experienced before.
Through the mentioned application, I tried to demonstrate acquired knowledge, so far. At the beginning, I faced the first, and also the most demanding client in my novice career, my dad. Considering that he does not understand programming, many requests were very creative but somewhat unrealistic. In the end we still managed to find a middle ground, and that's how KOKODA - GRUJIC application was born.

I will try to bring you closer to my dad's way of doing business, for whose needs the application was made. My dad delivers ordered eggs to the specified address. Customers can be other companies that have their own owners or they can be individuals. Individuals order by leaving their first and last name, possibly also a specific nickname and home address. While for companies, the order can be made by either the owner of the company or any employee, and the order is made by leaving the name of the company for which the delivery is made, as well as the address. In addition, my dad orders large quantities of food for laying hens of a certain type, for an exact time, in defined quantities of 1000 kg per package. Considering that he is sitll writing all this information down on paper, this app should make his job easier and store all this information for him.

# App features

The application is divided into two parts, part for admin and part for users. 

The administrator can log in with his credentials, and when he passes the check, he can access the following pages: Porucivanje jaja (Ordering eggs) and Porucivanje hrane (Ordering food). Within the Porucivanje jaja (Ordering eggs) page there is an Sve porudzbine (All orders) tab, where by clicking on it, all orders that have ever been made are listed. By clicking on the desired order, the details of the order are displayed (orderer name, city part, street, amount, price, orderer phone, do date, is order delivered..) and the option to change the order directly, if, for example, it has been delivered. By clicking on Neisporucene porudzbine (Undelivered orders), all parts of the city are listed, and by selecting a part of the city, you can see undelivered orders for that part of the city. Clicking on the Izmeni porudzbinu (Edit order) tab displays the list of orders available for modification, and the Obrisi porudzbinu (Delete order) tab displays the list of orders available for deletion. By clicking on the Porucioci (Customers) tab, the names of all customers are displayed, and by clicking on the customer's name, all his orders are attached. Ordering statistics and cost per person are displayed on the  Statisticki izvestaj (Statistical report) tab.
By clicking on the  Porucivanje hrane (Food ordering) tab (which is designed as a personal diary of the administrator's food orders for laying hens), the administrator can visit the following tabs: Porudzbine hrane (Food orders), which lists the months of the year, and by clicking on the desired month, the dates and quantity for ordered food are displayed (the amount of food ordered is fixed at 1000 kg by default), Kreiraj novu porudzbinu (Create new order) in which the admin can specify the date, month and type of food he ordered, on the delete order admin can delete the information about the order, on the tab Porucena hrana (Ordered food) admin can select the type of food for which he wants to view all orders, and on the Statisticki izvestaj (Statistics report) tab he can view detailed statistics of food orders and monthly cost.


A new user must create an account first, by clicking on the Registruj se (Register) tab, where he enters the name and surname of the owner of the company for which eggs are ordered, a phone number in a certain format (it is assumed that each person has the same phone number, and that it is not possible to create multiple accounts for the same phone number) and password. After successful registration, the user is redirected to the Prijava (Login) page where he needs to confirm used phone number (same format as when registarting) and password. After successfully logging in, the user on the Porucivanje jaja (Ordering eggs) tab makes his new order, where he enters the name of his company or his full name, date, part of the city and street where the order should be delivered, selects the number of pieces of eggs, and enters his phone number, in the same format as when registrating/logging in (this is just another form of verification). Administrator will be abled to see this new order.The user also has the option to subsequently send a note to the admin on the Posalji napomenu (Send note) tab where he can tell admin to to cancel the order, change the quantity, street and number, etc. and the admin can view read and unread notes, change the note to be read, and depending on the note, make changes to the order or delete one if needed.

# Testing

A built-in testing library was used for testing, and all its features are requested trought a "clojure.test :refer :all" call. Although I was supposed to write methods and tests for them at the same time, it turned out that I wrote the tests only after I coded the main part of the application (P.S I panicked). This is my first time coding tests for an app, and I love it.

# Problems I ran into

During the development of the application, I had several problems, some of them were of a technical nature but the others were related to my inexperience. My biggest problem was setting up the VS Code environment and installing the Calva dependency, where for some reason I couldn't run the REPL. I spent days trying to solve this problem, but in the end I implemented the project using the IntelliJ IDEA environment (which has its limitations).
The next bigger problem was the POST method, which did not read data from the form in the right format. I tried various solutions from stack overflow, watched video tutorials on YouTube, but there was no good solution at all. Then I turned for help to a colleague who at that time also started working on her project, she was luckier than me and managed to find a universal solution. Other problems were related to my lack of experience. I wanted to make a date entry through a calendar, I searched for solutions, found useful libraries and examples, but it was too demanding for my current knowledge of Clojure.
I wanted the ordered quantity to be read as a number, since I didn't know how to convert the text data into a number, I found another solution. My dad told me the number of pieces of eggs he sells in a package, so the desired packages can be selected through the drop-down menu which has its numerical implementation in the database. A similar problem appeared for recording the amount of ordered food, but it was easier to solve it, because dad always orders a fixed amount of food (1000 kg).
Another set of problems is related to HTML and CSS. I wanted to set the background on all pages, I found a good web programming book in Clojure that showed examples of solutions, but only in the end I realized that the architecture of my application needed to be very different to support that solution. Therefore, I adapted their solution to my application, created an HTML page and used the Selmer library to display that page. I tried several options for adjusting the spacing between tabs in the nav bar, but unfortunately I didn't get the best results.

The idea occurred to me that a nice functionality would be a selective search according to the customer's name. I wanted to realize it  by entering the name in the text field, and displaying the result accordingly. Since I could not implement the idea in such way, I came up with another solution. Now the names of all customers are listed on a separate tab, and by clicking on each one, their orders are displayed.

It was only after I gave my dad the app to test that I realized that some exceptions needed to be caught. Now the application takes care of potential errors when entering the wrong phone number and restricting the registration of multiple users on the same phone number (features aded for user registration and loggin in).

I had some problems when I tried to include Midje librarie for testing, so in the end I successfully wrote the tests using Clojure built-in testing library.

I created separate sessions for users and admin using ring.middleware.session, and the problem that still remains is that the admin and the user cannot use the application at the same time. I tried to solve this by creating the admin's credentials as local bindings and adding the user's id to his session, but without much success (a potential reason for the problem could be the duration of the session, or I should add application context, an example I saw in a book).

Only after discovering book "Web Development with Clojure" I realized that I could do many things much faster, more elegantly and with less effort. But at least this way, I learned everything and anything along the way.
The whole process of learning the language and creating the application has been going on continuously, in stages, since November. And even though this app is simple and modest, I'm very proud of what I've made and learned.

# What next?

The first and most important thing is to enable admin and users to use the application at the same time.
After that it would be nice to sort out the little things like dates, aesthetics, interactive search and things like that.

Pleading : Please keep in mind that this is my first real project, and specially it is written in Clojure which I also use for the first time. I would very much like to upgrade this application even more, but I will take a little break (temporarily) from working on this first version of the application, due to obligations for another subject.

## Prerequisites and Usage 

You should have Leiningen installed. To start the application you need to open terminal in the application folder, and type the command lein run. The application will start on the predefined port, but in case that address is already in bind, you should open the project, find src folder, core.clj file inside src folder, and server function within that file, there you will see predefined port number. Feel free to change it, and everything will work fine.

# References

## License

Copyright © 2022 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
