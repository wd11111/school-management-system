W aplikacji są nauczyciele, uczniowie, oceny i klasy.
Encje teacher i student posiadają relacje jeden do jeden z emailem z powodu autentyfikacji i aby nie było możliwości,
że teacher i student mają tego samego maila.
Encja teacher jest związana relacją jeden do wielu z przedmiotami które uczy,
ale róznież z encją TeacherInClass, która posiada pole Teacher, pole jednego przedmiotu i wielu klas,
w których tego przedmiotu uczy. Zostało to zaimplementowane w ten sposób aby nauczyciel mogł uczyć wielu przedmiotów,
ale aby było wiadomo jakiej klasy uczy konkretnego.
