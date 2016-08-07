package in.workarounds.resultset;

import com.google.auto.value.processor.AutoValueProcessor;
import com.google.testing.compile.JavaFileObjects;
import java.util.Arrays;
import java.util.Collections;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class AutoValueResultSetExtensionTest {

    @Test
    public void simple() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
                + "package test;\n"
                + "import com.google.auto.value.AutoValue;\n"
                + "import java.sql.ResultSet;\n"
                + "@AutoValue public abstract class Test {\n"
                + "  public static Test blah(ResultSet resultSet) { return null; }\n"
                + "  public abstract int a();\n"
                + "  public abstract String b();\n"
                + "}\n");

        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", ""
                + "package test;\n"
                + "import java.sql.ResultSet;\n"
                + "import java.lang.String;\n"
                + "final class AutoValue_Test extends $AutoValue_Test {\n"
                + "  AutoValue_Test(int a, String b) {\n"
                + "    super(a, b);\n"
                + "  }\n"
                + "  static AutoValue_Test createFromResultSet(ResultSet resultSet) {\n"
                + "    int a = resultSet.getInt(resultSet.findColumn(\"a\"));\n"
                + "    String b = resultSet.getString(resultSet.findColumn(\"b\"));\n"
                + "    return new AutoValue_Test(a, b);\n"
                + "  }\n"
                + "}\n");

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void columnName() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
                + "package test;\n"
                + "import com.gabrielittner.auto.value.resultSet.ColumnName;\n"
                + "import com.google.auto.value.AutoValue;\n"
                + "import java.sql.ResultSet;\n"
                + "@AutoValue public abstract class Test {\n"
                + "  public static Test blah(ResultSet resultSet) { return null; }\n"
                + "  public abstract int a();\n"
                + "  @ColumnName(\"column_b\") public abstract String b();\n"
                + "}\n");

        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", ""
                + "package test;\n"
                + "import java.sql.ResultSet;\n"
                + "import java.lang.String;\n"
                + "final class AutoValue_Test extends $AutoValue_Test {\n"
                + "  AutoValue_Test(int a, String b) {\n"
                + "    super(a, b);\n"
                + "  }\n"
                + "  static AutoValue_Test createFromResultSet(ResultSet resultSet) {\n"
                + "    int a = resultSet.getInt(resultSet.findColumn(\"a\"));\n"
                + "    String b = resultSet.getString(resultSet.findColumn(\"column_b\"));\n"
                + "    return new AutoValue_Test(a, b);\n"
                + "  }\n"
                + "}\n");

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void nullable() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
                + "package test;\n"
                + "import com.google.auto.value.AutoValue;\n"
                + "import java.sql.ResultSet;\n"
                + "import javax.annotation.Nullable;\n"
                + "@AutoValue public abstract class Test {\n"
                + "  public static Test blah(ResultSet resultSet) { return null; }\n"
                + "  public abstract int a();\n"
                + "  @Nullable public abstract String b();\n"
                + "}\n");

        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", ""
                + "package test;\n"
                + "import java.sql.ResultSet;\n"
                + "import java.lang.String;\n"
                + "final class AutoValue_Test extends $AutoValue_Test {\n"
                + "  AutoValue_Test(int a, String b) {\n"
                + "    super(a, b);\n"
                + "  }\n"
                + "  static AutoValue_Test createFromResultSet(ResultSet resultSet) {\n"
                + "    int a = resultSet.getInt(resultSet.findColumn(\"a\"));\n"
                + "    int bColumnIndex = resultSet.findColumn(\"b\");"
                + "    String b = resultSet.isNull(bColumnIndex) ? null : resultSet.getString(bColumnIndex);\n"
                + "    return new AutoValue_Test(a, b);\n"
                + "  }\n"
                + "}\n");

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void nullableColumnName() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
                + "package test;\n"
                + "import com.gabrielittner.auto.value.resultSet.ColumnName;\n"
                + "import com.google.auto.value.AutoValue;\n"
                + "import java.sql.ResultSet;\n"
                + "import javax.annotation.Nullable;\n"
                + "@AutoValue public abstract class Test {\n"
                + "  public static Test blah(ResultSet resultSet) { return null; }\n"
                + "  public abstract int a();\n"
                + "  @Nullable @ColumnName(\"column_b\") public abstract String b();\n"
                + "}\n");

        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", ""
                + "package test;\n"
                + "import java.sql.ResultSet;\n"
                + "import java.lang.String;\n"
                + "final class AutoValue_Test extends $AutoValue_Test {\n"
                + "  AutoValue_Test(int a, String b) {\n"
                + "    super(a, b);\n"
                + "  }\n"
                + "  static AutoValue_Test createFromResultSet(ResultSet resultSet) {\n"
                + "    int a = resultSet.getInt(resultSet.findColumn(\"a\"));\n"
                + "    int bColumnIndex = resultSet.findColumn(\"column_b\");"
                + "    String b = resultSet.isNull(bColumnIndex) ? null : resultSet.getString(bColumnIndex);\n"
                + "    return new AutoValue_Test(a, b);\n"
                + "  }\n"
                + "}\n");

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void unsupported() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
                + "package test;\n"
                + "import com.google.auto.value.AutoValue;\n"
                + "import java.sql.ResultSet;\n"
                + "@AutoValue public abstract class Test {\n"
                + "  public static Test blah(ResultSet resultSet) { return null; }\n"
                + "  public abstract int[] a();\n"
                + "  public abstract String b();\n"
                + "}\n");

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor())
                .failsToCompile()
                .withErrorContaining("Property has type that can't be read from ResultSet.");
    }

    @Test
    public void unsupportedWithNullable() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
                + "package test;\n"
                + "import com.google.auto.value.AutoValue;\n"
                + "import java.sql.ResultSet;\n"
                + "import javax.annotation.Nullable;\n"
                + "@AutoValue public abstract class Test {\n"
                + "  public static Test blah(ResultSet resultSet) { return null; }\n"
                + "  @Nullable public abstract int[] a();\n"
                + "  public abstract String b();\n"
                + "}\n");

        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", ""
                + "package test;\n"
                + "import java.sql.ResultSet;\n"
                + "import java.lang.String;\n"
                + "final class AutoValue_Test extends $AutoValue_Test {\n"
                + "  AutoValue_Test(int[] a, String b) {\n"
                + "    super(a, b);\n"
                + "  }\n"
                + "  static AutoValue_Test createFromResultSet(ResultSet resultSet) {\n"
                + "    int[] a = null; // can't be read from resultSet\n"
                + "    String b = resultSet.getString(resultSet.findColumn(\"b\"));\n"
                + "    return new AutoValue_Test(a, b);\n"
                + "  }\n"
                + "}\n");

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void allResultSetTypes() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
                + "package test;\n"
                + "import com.google.auto.value.AutoValue;\n"
                + "import java.sql.ResultSet;\n"
                + "@AutoValue public abstract class Test {\n"
                + "  public static Test blah(ResultSet resultSet) { return null; }\n"
                + "  public abstract String a();\n"
                + "  public abstract int b();\n"
                + "  public abstract Integer c();\n"
                + "  public abstract long d();\n"
                + "  public abstract Long e();\n"
                + "  public abstract short f();\n"
                + "  public abstract Short g();\n"
                + "  public abstract double h();\n"
                + "  public abstract Double i();\n"
                + "  public abstract float j();\n"
                + "  public abstract Float k();\n"
                + "  public abstract boolean l();\n"
                + "  public abstract Boolean m();\n"
                + "  public abstract byte[] n();\n"
                + "}\n");

        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", ""
                + "package test;\n"
                + "import java.sql.ResultSet;\n"
                + "import java.lang.Boolean;\n"
                + "import java.lang.Double;\n"
                + "import java.lang.Float;\n"
                + "import java.lang.Integer;\n"
                + "import java.lang.Long;\n"
                + "import java.lang.Short;\n"
                + "import java.lang.String;\n"
                + "final class AutoValue_Test extends $AutoValue_Test {\n"
                + "  AutoValue_Test(String a, int b, Integer c, long d, Long e, short f, Short g, double h, Double i, float j, Float k, boolean l, Boolean m, byte[] n) {\n"
                + "    super(a, b, c, d, e, f, g, h, i, j, k, l, m, n);\n"
                + "  }\n"
                + "  static AutoValue_Test createFromResultSet(ResultSet resultSet) {\n"
                + "    String a = resultSet.getString(resultSet.findColumn(\"a\"));\n"
                + "    int b = resultSet.getInt(resultSet.findColumn(\"b\"));\n"
                + "    Integer c = resultSet.getInt(resultSet.findColumn(\"c\"));\n"
                + "    long d = resultSet.getLong(resultSet.findColumn(\"d\"));\n"
                + "    Long e = resultSet.getLong(resultSet.findColumn(\"e\"));\n"
                + "    short f = resultSet.getShort(resultSet.findColumn(\"f\"));\n"
                + "    Short g = resultSet.getShort(resultSet.findColumn(\"g\"));\n"
                + "    double h = resultSet.getDouble(resultSet.findColumn(\"h\"));\n"
                + "    Double i = resultSet.getDouble(resultSet.findColumn(\"i\"));\n"
                + "    float j = resultSet.getFloat(resultSet.findColumn(\"j\"));\n"
                + "    Float k = resultSet.getFloat(resultSet.findColumn(\"k\"));\n"
                + "    boolean l = resultSet.getInt(resultSet.findColumn(\"l\")) == 1;\n"
                + "    Boolean m = resultSet.getInt(resultSet.findColumn(\"m\")) == 1;\n"
                + "    byte[] n = resultSet.getBlob(resultSet.findColumn(\"n\"));\n"
                + "    return new AutoValue_Test(a, b, c, d, e, f, g, h, i, j, k, l, m, n);\n"
                + "  }\n"
                + "}\n");

        assertAbout(javaSources())
                .that(Collections.singletonList(source))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void resultSetAdapter() {
        JavaFileObject fooClass = JavaFileObjects.forSourceString("test.Foo", ""
                + "package test;\n"
                + "public class Foo {\n"
                + "  public final String data;\n"
                + "  public Foo(String data) {\n"
                + "    this.data = data;\n"
                + "  }\n"
                + "}\n");
        JavaFileObject fooFactorySource = JavaFileObjects.forSourceString("test.FooAdapter", ""
                + "package test;\n"
                + "import android.content.ContentValues;\n"
                + "import java.sql.ResultSet;\n"
                + "import com.gabrielittner.auto.value.resultSet.ColumnTypeAdapter;\n"
                + "public class FooAdapter implements ColumnTypeAdapter<Foo> {\n"
                + "  public Foo fromResultSet(ResultSet resultSet, String columnName) {\n"
                + "    return new Foo(resultSet.getString(resultSet.getColumnIndex(columnName)));\n"
                + "  }\n"
                + "  public void toContentValues(ContentValues values, String columnName, Foo value) {\n"
                + "  }\n"
                + "}\n");
        JavaFileObject stringFactorySource = JavaFileObjects.forSourceString("test.StringAdapter", ""
                + "package test;\n"
                + "import android.content.ContentValues;\n"
                + "import java.sql.ResultSet;\n"
                + "import com.gabrielittner.auto.value.resultSet.ColumnTypeAdapter;\n"
                + "public class StringAdapter implements ColumnTypeAdapter<String> {\n"
                + "  public String fromResultSet(ResultSet resultSet, String columnName) {\n"
                + "    return resultSet.getString(resultSet.getColumnIndex(columnName));\n"
                + "  }\n"
                + "  public void toContentValues(ContentValues values, String columnName, String value) {\n"
                + "  }\n"
                + "}\n");
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
                + "package test;\n"
                + "import com.gabrielittner.auto.value.resultSet.ColumnName;\n"
                + "import com.gabrielittner.auto.value.resultSet.ColumnAdapter;\n"
                + "import com.google.auto.value.AutoValue;\n"
                + "import javax.annotation.Nullable;\n"
                + "import java.sql.ResultSet;\n"
                + "@AutoValue public abstract class Test {\n"
                + "  public static Test blah(ResultSet resultSet) { return null; }\n"
                + "  @ColumnAdapter(FooAdapter.class) public abstract Foo foo();\n"
                + "  @ColumnAdapter(StringAdapter.class) public abstract String bar();\n"
                + "  @ColumnAdapter(StringAdapter.class) @ColumnName(\"column\") public abstract String columnName();\n"
                + "}\n");
        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", ""
                + "package test;\n"
                + "import java.sql.ResultSet;\n"
                + "import java.lang.String;\n"
                + "final class AutoValue_Test extends $AutoValue_Test {\n"
                + "  AutoValue_Test(Foo foo, String bar, String columnName) {\n"
                + "    super(foo, bar, columnName);\n"
                + "  }\n"
                + "  static AutoValue_Test createFromResultSet(ResultSet resultSet) {\n"
                + "    FooAdapter fooAdapter = new FooAdapter();\n"
                + "    StringAdapter stringAdapter = new StringAdapter();\n"
                + "    Foo foo = fooAdapter.fromResultSet(resultSet, \"foo\");\n"
                + "    String bar = stringAdapter.fromResultSet(resultSet, \"bar\");\n"
                + "    String columnName = stringAdapter.fromResultSet(resultSet, \"column\");\n"
                + "    return new AutoValue_Test(foo, bar, columnName);\n"
                + "  }\n"
                + "}\n");

        assertAbout(javaSources())
                .that(Arrays.asList(fooClass, stringFactorySource, fooFactorySource, source))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void rxjava() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
                + "package test;\n"
                + "import com.google.auto.value.AutoValue;\n"
                + "import java.sql.ResultSet;\n"
                + "@AutoValue public abstract class Test {\n"
                + "  public static Test blah(ResultSet resultSet) { return null; }\n"
                + "  public abstract int a();\n"
                + "  public abstract String b();\n"
                + "}\n");

        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", ""
                + "package test;\n"
                + "import java.sql.ResultSet;\n"
                + "import java.lang.Override;\n"
                + "import java.lang.String;\n"
                + "import rx.functions.Func1;\n"
                + "final class AutoValue_Test extends $AutoValue_Test {\n"
                + "  static final Func1<ResultSet, Test> MAPPER = new Func1<ResultSet, Test>() {\n"
                + "    @Override\n"
                + "    public AutoValue_Test call(ResultSet c) {\n"
                + "      return createFromResultSet(c);\n"
                + "    }\n"
                + "  };\n"
                + "  AutoValue_Test(int a, String b) {\n"
                + "    super(a, b);\n"
                + "  }\n"
                + "  static AutoValue_Test createFromResultSet(ResultSet resultSet) {\n"
                + "    int a = resultSet.getInt(resultSet.findColumn(\"a\"));\n"
                + "    String b = resultSet.getString(resultSet.findColumn(\"b\"));\n"
                + "    return new AutoValue_Test(a, b);\n"
                + "  }\n"
                + "}\n");

        assertAbout(javaSources())
                .that(Arrays.asList(func1(), source))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    @Test
    public void rxjavaOptIn() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
                + "package test;\n"
                + "import com.google.auto.value.AutoValue;\n"
                + "import java.sql.ResultSet;\n"
                + "import rx.functions.Func1;\n"
                + "@AutoValue public abstract class Test {\n"
                + "  public static Func1<ResultSet, Test> blahMap() { return null; }\n"
                + "  public abstract int a();\n"
                + "  public abstract String b();\n"
                + "}\n");

        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", ""
                + "package test;\n"
                + "import java.sql.ResultSet;\n"
                + "import java.lang.Override;\n"
                + "import java.lang.String;\n"
                + "import rx.functions.Func1;\n"
                + "final class AutoValue_Test extends $AutoValue_Test {\n"
                + "  static final Func1<ResultSet, Test> MAPPER = new Func1<ResultSet, Test>() {\n"
                + "    @Override\n"
                + "    public AutoValue_Test call(ResultSet c) {\n"
                + "      return createFromResultSet(c);\n"
                + "    }\n"
                + "  };\n"
                + "  AutoValue_Test(int a, String b) {\n"
                + "    super(a, b);\n"
                + "  }\n"
                + "  static AutoValue_Test createFromResultSet(ResultSet resultSet) {\n"
                + "    int a = resultSet.getInt(resultSet.findColumn(\"a\"));\n"
                + "    String b = resultSet.getString(resultSet.findColumn(\"b\"));\n"
                + "    return new AutoValue_Test(a, b);\n"
                + "  }\n"
                + "}\n");

        assertAbout(javaSources())
                .that(Arrays.asList(func1(), source))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    private JavaFileObject func1() {
        return JavaFileObjects.forSourceString(
                "rx.functions.Func1", ""
                        + "package rx.functions;\n"
                        + "public interface Func1<T, R> {\n"
                        + "  R call(T t);\n"
                        + "}\n");
    }

    @Test
    public void generatesNothingWithoutOptIn() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", ""
                + "package test;\n"
                + "import com.google.auto.value.AutoValue;\n"
                + "import java.sql.ResultSet;\n"
                + "@AutoValue public abstract class Test {\n"
                + "  public abstract int a();\n"
                + "  public abstract String b();\n"
                + "}\n");

        JavaFileObject expected = JavaFileObjects.forSourceString("test.AutoValue_Test", ""
                + "package test;\n"
                + "import javax.annotation.Generated;\n"
                + "@Generated(\"com.google.auto.value.processor.AutoValueProcessor\")\n"
                + "final class AutoValue_Test extends Test {\n"
                + "  private final int a;\n"
                + "  private final String b;\n"
                + "  AutoValue_Test(\n"
                + "      int a,\n"
                + "      String b) {\n"
                + "    this.a = a;\n"
                + "    if (b == null) {\n"
                + "      throw new NullPointerException(\"Null b\");\n"
                + "    }\n"
                + "    this.b = b;\n"
                + "  }\n"
                + "  @Override\n"
                + "  public int a() {\n"
                + "    return a;\n"
                + "  }\n"
                + "  @Override\n"
                + "  public String b() {\n"
                + "    return b;\n"
                + "  }\n"
                + "  @Override\n"
                + "  public String toString() {\n"
                + "    return \"Test{\"\n"
                + "        + \"a=\" + a + \", \"\n"
                + "        + \"b=\" + b\n"
                + "        + \"}\";\n"
                + "  }\n"
                + "  @Override\n"
                + "  public boolean equals(Object o) {\n"
                + "    if (o == this) {\n"
                + "      return true;\n"
                + "    }\n"
                + "    if (o instanceof Test) {\n"
                + "      Test that = (Test) o;\n"
                + "      return (this.a == that.a())\n"
                + "           && (this.b.equals(that.b()));\n"
                + "    }\n"
                + "    return false;\n"
                + "  }\n"
                + "  @Override\n"
                + "  public int hashCode() {\n"
                + "    int h = 1;\n"
                + "    h *= 1000003;\n"
                + "    h ^= this.a;\n"
                + "    h *= 1000003;\n"
                + "    h ^= this.b.hashCode();\n"
                + "    return h;\n"
                + "  }\n"
                + "}\n");

        assertAbout(javaSources())
                .that(Collections.singleton(source))
                .processedWith(new AutoValueProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }
}
