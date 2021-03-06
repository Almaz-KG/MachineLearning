#### Решающие деревья

Бинарное дерево - это бинарный ациклический граф

Решающее дерево — это логический алгоритм классификации, основанный на поиске конъюнктивных закономерностей.

Бинарное решающее дерево — это алгоритм классификации, задающийся бинарным деревом, в котором каждой внутренней вершине приписан предикат, каждой терминальной вершине приписано имя класса

Преимущество деревьев решений в том, что они легко интерпретируемы, понятны человеку

#### Описание алгоритмов построения деревьев решений
##### Алгоритм ID3
Идея алгоритма заключается в последовательном дроблении выборки на две части до тех пор, пока в каждой части не окажутся объекты только одного класса. Проще всего записать этот алгоритм в виде рекурсивной процедуры LearnID3, которая строит дерево по заданной подвыборке S. Для построения полного дерева она применяется ко всей выборке и возвращает указатель на корень построенного дерева:
> v0 := LearnID3 (Xl).

На практике применяются различные критерии ветвления.
1. Критерий, ориентированный на скорейшее отделение одного из классов.
2. Критерий, ориентированный на задачи с большим числом классов. Фактически, это обобщение статистического определения информативности.
3. D-критерий — число пар объектов из разных классов, на которых предикат β принимает разные значения. В случае двух классов он имеет вид


> Алгоритм Рекурсивный алгоритм построения решающего дерева ID3 Вход:
S — обучающая выборка;
В — множество базовых предикатов;
Выход:
    Возвращает корневую вершину дерева, построенного по выборке S;

```   
    1: ПРОЦЕДУРА LearnID3 (S);
    2:      если все объекты из S лежат в одном классе с  Y то
    3:          создать новый лист v;
    4:          cv:=c;
    5:          вернуть (v);
    6:      найти предикат с максимальной информативностью:
    7:           β:= arg max | (β | S);
    8:          S_0, S_1: разбить выборку на две части S = S_0 U S_1 по предикату β:
    9:          если S_0 == empty или S_1 == empty то
    10:             создать новый лист v;
    11:             cv :=  класс, в котором находится большинство объектов из S;
    12:             вернуть (v);
    13:         иначе
    14:             создать новую внутреннюю вершину v;
    15:             βv:= β;
    16:             Lv := LearnID3 (S_0);   (построить левое поддерево)
    17:             Rv := LearnID3 (S_1);   (построить правое поддерево)
    18:             прикрепить Lv и Rv к родительскому узлу v
    19:             вернуть (v);
```

Преимущества алгоритма ID3
- Простота и интерпретируемость классификации. Алгоритм способен не только классифицировать объект, но и выдать объяснение классификации в терминах предметной области. Для этого достаточно записать последовательность условий, пройденных объектом от корня дерева до листа.
- Алгоритм синтеза решающего дерева имеет сложность, линейную по длине выборки.
- Если множество предикатов В настолько богато, что на шаге 6 всегда находится предикат, разбивающий выборку S на непустые подмножества S_0 и S_1,то алгоритм строит бинарное решающее дерево, безошибочно классифицирующее выборку Xl.
- Алгоритм очень прост для реализации и легко поддаётся различным усовершенствованиям. Можно использовать различные критерии ветвления и критерии останова, вводить редукцию, и т. д.

Недостатки алгоритма ID3
- Жадность. Локально оптимальный выбор предиката βV не является глобально оптимальным. В случае выбора неоптимального предиката алгоритм не способен вернуться на уровень вверх и заменить неудачный предикат.
- Чем дальше вершина v расположена от корня дерева, тем меньше длина подвыборки S, по которой приходится принимать решение о ветвлении в вершине v. Тем менее статистически надёжным является выбор предиката βV.
- Алгоритм склонен к переобучению — как правило, он переусложняет структуру дерева. Обобщающая способность алгоритма (качество классификации новыхобъектов) относительно невысока.

Основная причина недостатков — неоптимальность жадной стратегии наращивания дерева. Перечисленные недостатки в большей или меньшей степени свойственны большинству алгоритмов синтеза решающих деревьев. Для их устранения применяют различные эвристические приемы: редукцию, элементы глобальной оптимизации, `заглядывание вперёд (look ahead)`.


##### Алгоритм  С4.5
Прежде чем приступить к описанию алгоритма построения дерева решений, определим обязательные требования к структуре данных и непосредственно к самим данным, при выполнении которых алгоритм C4.5 будет работоспособен:

- Описание атрибутов. Данные, необходимые для работы алгоритма, должны быть представлены в виде плоской таблицы. Вся информация об объектах (далее примеры) из предметной области должна описываться в виде конечного набора признаков (далее атрибуты). Каждый атрибут должен иметь дискретное или числовое значение. Сами атрибуты не должны меняться от примера к примеру, и количество атрибутов должно быть фиксированным для всех примеров.
- Определенные классы. Каждый пример должен быть ассоциирован с конкретным классом, т.е. один из атрибутов должен быть выбран в качестве метки класса.
- Дискретные классы. Классы должны быть дискретными, т.е. иметь конечное число значений. Каждый пример должен однозначно относиться к конкретному классу. Случаи, когда примеры принадлежат к классу с вероятностными оценками, исключаются. Количество классов должно быть значительно меньше количества примеров.

Пусть нам задано множество примеров T, где каждый элемент этого множества описывается m атрибутами. Количество примеров в множестве T будем называть мощностью этого множества и будем обозначать |T|. 
Пусть метка класса принимает следующие значения C1, C2 … Ck.

Наша задача будет заключаться в построении иерархической классификационной модели в виде дерева из множества примеров T. Процесс построения дерева будет происходить сверху вниз. Сначала создается корень дерева, затем потомки корня и т.д. 
На первом шаге мы имеем пустое дерево (имеется только корень) и исходное множество T (ассоциированное с корнем). Требуется разбить исходное множество на подмножества. Это можно сделать, выбрав один из атрибутов в качестве проверки. Тогда в результате разбиения получаются n(по числу значений атрибута) подмножеств и, сответственно, создаются n потомков корня, каждому из которых поставлено в соответствие свое подмножество, полученное при разбиении множества T. Затем эта процедура рекурсивно применяется ко всем подмножествам (потомкам корня) и т.д.

```   
    1:  ПРОЦЕДУРА LearnС45 (S);
    2:      для всех v из V
    3:          Sv:= подмножество объектов X, дошедщих до v
    4:          если Sv пусто то
    5:              создать новый лист v;
    6:              cv :=  класс, в котором находится большинство объектов из S;
    7:              вернуть (v);
    8:          число ошибок при классификации Sv, различными способами
    9:              r(v) - поддеревом, растущим из вершины v
    10:             rL(v) - поддеревом, левой дочерней вершины v
    10:             rR(v) - поддеревом, правой дочерней вершины v
    11:             rс(v) - классу с из Y
    12:         В зависимости от того, какое из способов имеет минимальное значение:
    13:             сохранить поддерево v
    14:             заменить поддерево v поддеревом Lv
    15:             заменить поддерево v поддеревом Rv
    16:             заменить поддерево v листом cv: = arg min(rc(v))
```

##### Алгоритм CART
CART (сокращение от Classification And Regression Tree, переводится как 'Дерево Классификации и Регрессии') – алгоритм бинарного дерева решений, предназначеный для решения задач классификации и регрессии. Существует также несколько модифицированных версий – алгоритмы IndCART и DB-CART. Алгоритм DB-CART базируется на следующей идее: вместо того чтобы использовать обучающий набор данных для определения разбиений, используем его для оценки распределения входных и выходных значений и затем используем эту оценку, чтобы определить разбиения. DB, соответственно означает – 'distribution based'. Утверждается, что эта идея дает значительное уменьшение ошибки классификации, по сравнению со стандартными методами построения дерева. Основными отличиями алгоритма CART от алгоритмов семейства ID3 являются:
- бинарное представление дерева решений;
- функция оценки качества разбиения;
- механизм отсечения дерева;
- алгоритм обработки пропущенных значений;
- построение деревьев регрессии.

В алгоритме CART каждый узел дерева решений имеет двух потомков. На каждом шаге построения дерева правило, формируемое в узле, делит заданное множество примеров (обучающую выборку) на две части – часть, в которой выполняется правило (потомок – right) и часть, в которой правило не выполняется (потомок – left). Для выбора оптимального правила используется функция оценки качества разбиения.
Обучение дерева решений относится к классу обучения с учителем, то есть обучающая и тестовая выборки содержат классифицированный набор примеров. Оценочная функция, используемая алгоритмом CART, базируется на интуитивной идее уменьшения нечистоты (неопределённости) в узле. Рассмотрим задачу с двумя классами и узлом, имеющим по 50 примеров одного класса. Узел имеет максимальную 'нечистоту'. Если будет найдено разбиение, которое разбивает данные на две подгруппы 40:5 примеров в одной и 10:45 в другой, то интуитивно 'нечистота' уменьшится. Она полностью исчезнет, когда будет найдено разбиение, которое создаст подгруппы 50:0 и 0:50. В алгоритме CART идея 'нечистоты' формализована в индексе Gini. Если набор данных Т содержит данные n классов, тогда индекс Gini определяется как:

> Индекс Gini
>   Gini = 1 - Sum(i=1->n) pi^2
>   
>    где pi – вероятность (относительная частота) класса i в T.

Наилучшим считается то разбиение, для которого GiniSplit(T) минимально.

Вектор предикторных переменных, подаваемый на вход дерева может содержать как числовые (порядковые) так и категориальные переменные. В любом случае в каждом узле разбиение идет только по одной переменной. Если переменная числового типа, то в узле формируется правило вида xi <= c. Где с – некоторый порог, который чаще всего выбирается как среднее арифметическое двух соседних упорядоченных значений переменной xi обучающей выборки. Если переменная категориального типа, то в узле формируется правило xi V(xi), где V(xi) – некоторое непустое подмножество множества значений переменной xi в обучающей выборке. Следовательно, для n значений числового атрибута алгоритм сравнивает n-1 разбиений, а для категориального (2n-1 – 1). На каждом шаге построения дерева алгоритм последовательно сравнивает все возможные разбиения для всех атрибутов и выбирает наилучший атрибут и наилучшее разбиение для него.

Механизм отсечения дерева, оригинальное название minimal cost-complexity tree pruning, – наиболее серьезное отличие алгоритма CART от других алгоритмов построения дерева. CART рассматривает отсечение как получение компромисса между двумя проблемами: получение дерева оптимального размера и получение точной оценки вероятности ошибочной классификации.
Основная проблема отсечения – большое количество всех возможных отсеченных поддеревьев для одного дерева. Более точно, если бинарное дерево имеет |T| – листов, тогда существует `~[1.5^|T|]` отсечённых поддеревьев. И если дерево имеет хотя бы 1000 листов, тогда число отсечённых поддеревьев становится просто огромным.
Базовая идея метода – не рассматривать все возможные поддеревья, ограничившись только `лучшими представителями` по определенной оценке.

##### Преимущества решающих деревьев
- Интерпретируемость
- Допускаются разнотипные данные
- Возможность обхода пропусков

##### Недостатки решающих деревьев
- Переобучение
- Фрагментация
- Неустойчивость к шуму, составу выборки, критерию

##### Способы устранения недостатков
- Редукция
- Композиция (леса) деревьев

### Источники
- [Открытый курс машинного обучения. Тема 3. Классификация, деревья решений и метод ближайших соседей](https://habr.com/company/ods/blog/322534/)
- [Методы построения деревьев решений в задачах классификации в Data Mining](https://ami.nstu.ru/~vms/lecture/data_mining/trees.htm#_Toc123289771)