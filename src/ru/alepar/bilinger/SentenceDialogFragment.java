/*
 * Copyright Stanislav Belogolov 2012.
 *
 * This file is a part of Bilinger. Bilinger is free software distributed under
 * the terms of GNU General Public License version 3. You should have received
 * a copy of the GNU General Public License along with Bilinger. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package ru.alepar.bilinger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class SentenceDialogFragment extends DialogFragment {
  private final String nativeString;
  private final String foreignString;

  public SentenceDialogFragment(String nativeString, String foreignString) {
    this.nativeString = nativeString;
    this.foreignString = foreignString;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return new AlertDialog.Builder(getActivity())
        .setMessage(foreignString + "\n\n" + nativeString)
        .create();
  }
}
