/*fpkg*/package foo.bar {
	import pkg.Stuff;
	/**
	 * java doc
	 */
	public class /*f*/Blat extends Bing implements Febraz, Goo {

		private var data:String;
		private var data:* = womble();

		public function Blat() {
			super();
			return;
		}

		[Annotate]
		[Basic()]
		[String("foo")]
		[Num(1)]
		[Bool(false)]
		[Arg(foo="bar")]
		[List(foo="bar", that=2)]
		[Event("alpha")]
		[Event("beta")]
		private var x:String = 1;
		/** javadoc? */
		public static function func(arg:Number, foo=null, ...):Boolean {
			default xml namespace = "http://example.com/";
			for (var b=1;b<=10;b++) { bar(); }
			for (;;) { break; }
			for (var g in blah) { r(); }
			for each (var g in blah) { r(); }
			if (h==undefined) { throw new Error(); } else { /* bar */ }
			if (a) b();
			while (false) v();
			do { continue; } while (m);
			switch (blah) {
				case 3: x(); y();
				case 4: next;
				default: x(); y();
			}
			try {
				with (scope) foo();
			} catch (e) {
				var a=b?(x+y):new Foo();
			} catch (f:Error) {
				const X=new Thing();
			}

            // anonimus funciton
            var anonFunc : function = function (  ) : void {
              // body
            };			


            var fl : number = 1.23;
            var he : number = 0xFFFFFF;

            delete anonFunc;

			try {
				foo();
			} finally {
				ff = function(y, z) { };
			}
			l=[1,'2'];
			xx = doc.ns::name;
			m={a:null};
// TODO:			hex = 0x3;
			d = <foo bar="{blat}"/>;
			r = /regexp/;
			a = b.(@c);
			zz = a..b;
			zzz = a..@*;
			zzzz = a..@["foo"+b];
			/* unary expressions */
			--a; ++a; a--; a++; a = -a;
			return a.call(i.j*k, l[m]);
		}
	}
} // trailing comment