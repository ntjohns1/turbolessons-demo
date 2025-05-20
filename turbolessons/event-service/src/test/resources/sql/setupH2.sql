DROP ALIAS IF EXISTS DATE;
CREATE ALIAS DATE AS '
String date(String s) {
    return s.split(" ")[0];
}';
