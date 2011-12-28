/*
 * Copyright Stanislav Belogolov 2011.
 *
 * This file is a part of Bilinger. Bilinger is free software distributed under
 * the terms of GNU General Public License version 3. You should have received
 * a copy of the GNU General Public License along with Bilinger. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package ru.alepar.bilinger;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
