# jbus
今天解析了Otto源码，发现有1个地方不是很友好，就是post的时候只能传入一个参数，Object。于是乎呢，本人参考Otto源码，自己简单写了一个类似Otto的总线框架，JBus。

## usage
    public class MainActivity extends AppCompatActivity {

        private EditText input;
        private Button post;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Bus.INSTANCE.register(this);
            Bus.INSTANCE.post("test2", 25);
            input = (EditText) findViewById(R.id.input);
            post = (Button) findViewById(R.id.post);
            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bus.INSTANCE.post("test1", input.getText().toString());
                }
            });
        }

        @Subscribe("test1")
        public void test1(String name) {
            Log.e(getClass().getName(), "name : " + name);
            Toast.makeText(this, name, Toast.LENGTH_LONG).show();
        }

        @Subscribe("test2")
        public void test2(int age) {
            Log.e(getClass().getName(), "age : " + age);
            Toast.makeText(this, String.valueOf(age), Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onDestroy() {
            Bus.INSTANCE.unregister(this);
            super.onDestroy();
        }

    }
    
### 初步测试，可以无Bug运行，也达到了Otto的效果。
