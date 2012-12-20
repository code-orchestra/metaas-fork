package com.eliseyev.test {

    import com.eliseyev.globaltest.enterFrameBroadcaster;

	public class ParseTest extends AwesomeClass {

		private var stuff : TestOutOfPackageClass;

		protected namespace $protected_data;

		use namespace $protected_data;

		public function myTestMethod(xml : XML) : void {

			var myObject : Object = new flash.display.FrameLabel("test");
			flash.utils.setTimeout( null, 0 );

			trace(_DEFAULT_BUNDLE.charAt(1));
			myOOPFunction().charAt(1);
			var oopClass : TestOutOfPackageClass;
			var oopInterface : TestOutOfPackageInterface;

			var value : Number = 9;
			trace(this.value.toString() + '%');

			for each ( var v:String in this ) {
				if ( v is String ) {
					throw new ArgumentError();
				}
			}


			if (1 == 2) {
			}

			if (1 != 2) {
			}

			if (1 > 2) {
			}

			var disp : IMyDispatcher = null;
			disp.addEventListener(null, null);

			var i : int = 0, j : int = 0;

			for (i = 0;i < 16;i++) {
			}

			var resource:*;
			var resourceClass:Class = resource as Class;

			try {
				resource = new resourceClass( 0, 0 );
			} catch ( e:Error ) {
				resource = new resourceClass();
			}
		}

	}
}

internal const _DEFAULT_BUNDLE:String = "Test out-of-package constant";

trace("Out-of-package statement test", this._DEFAULT_BUNDLE, this.myOOPFunction());

internal function myOOPFunction() : String {
  return "Out-of-package function test";
}

internal final class TestOutOfPackageClass {
}

internal interface TestOutOfPackageInterface {
}