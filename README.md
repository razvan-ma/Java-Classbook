# Java-Classbook
Clasa Catalog:
− Am folosit design pattern-ul singleton, care imi asigura ca exista o singura
instanta a catalogului in toata tema.
Clasa User si mostenirile sale:

- Am suprascris metoda equals deoarece metoda din clasa Object compara
referintele celor 2 useri, iar in tema se vor compara obiecte user cu nume
identic, dar avand adrese diferite, va trebui sa le compar dupa nume.
- UserFactory are o implementare similara celei din laborator.

Clasa Grade:
− Functia compareTo, impusa de implementarea interfetei Comparable
compara Grade-urile dupa nota totala calculata prin suma dintre nota
partiala si in examen.

Clasa Group:
− Extinde o colectie ordonata(ArrayList).
− Poate fi instantiata cu un comparator sau fara, comparator care sorteaza
dupa numele de familie.

Clasa Course:
− getGrade returneaza nota cu numele studentului dat ca parametru
− getAllStudents returneaza o lista cu toti studentii cursului, uitandu-se in
lista de note puse cursului respectiv.
− CourseBuilder foloseste modelul de la laborator si permite crearea mai
simpla a unui curs.
− Cele doua clase care mostenesc clasa Course implementeaza builder-ul si
specifica metoda prin care studentii trec cursul cu metoda
getGraduatedStudents.

Sablonul Observer:
− Cand se citeste din fisierul JSON fiecare parinte este introdus in lista de
observeri din catalog
− parintii au un camp de notificare unde se afla notificarea pentru studentul
sau.
− notifyObservers schimba campul notification al parintelui cu nota care se
schimba (in clasa ScoreVisitor)

Sablonul Strategy:
− Se citeste tipul strategiei din fiecare curs ca un string, dupa care se decide
ce tip de obiect care mosteneste clasa abstracta Strategy sa se instantieze.
− In pagina Teacher se stabileste cel mai bun student folosind strategia
respectiva cursului.

Sablonul Visitor:
− In metodele addPartialGrade si addExamGrade se adauga note din fisierul
JSON, creand o noua pereche in dictionar si initalizand valoarea(ArrayList)
daca nu exista profesorul/asistentul deja, daca exista se adauga un element
in lista.
− Metodele visit parcurg Lista de Tuple-uri (note) si le adauga pe rand in
catalog, instantiind un nou grade daca celelalt User (teacher/assistant) nu a
pus nota sa in catalog.
− Daca respectivul a pus nota, se completeaza doar campul score al celui care
viziteaza momentan.

Sablonul Memento:
− In makeBackup creeaza o noua lista continand notele la mementoul
respectiv, si undo readuce lista de note la mementoul in care se s-a apelat
makeBackup, doar daca undo a fost apelat(lista din Snapshot nu e goala).

Student Page:
− Creeaza o pagina care contine toate cursurile la care se afla studentul,
apasarea pe un element din lista duce la afisarea detaliilor despre curs si
nota studentului la acel curs.

Teacher/Assitant Page:
− Creeaza o pagina care contine toate cursurile la care se afla
profesorul/asistentul, si butonul de visit apleaza functia visit pentru user si
afiseaza toate notele puse in catalog la acel curs de catre user, pentru
teacher afiseaza si cel mai bun student utilizand Stragety.

Parent Page:
− De cate ori copilul parintelui primeste o nota, se poate apasa pe butonul de
vazut notificari care arata parintelui ce note a primit studentul. 
