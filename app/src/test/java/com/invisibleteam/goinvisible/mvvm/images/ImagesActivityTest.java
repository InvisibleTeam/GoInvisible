package com.invisibleteam.goinvisible.mvvm.images;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.helper.SharingHelper;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;
import com.invisibleteam.goinvisible.mvvm.images.tablet.TabletEditViewModel;
import com.invisibleteam.goinvisible.mvvm.images.tablet.TabletImagesViewModel;
import com.invisibleteam.goinvisible.mvvm.settings.SettingsActivity;
import com.invisibleteam.goinvisible.util.TagsManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ImagesActivityTest {

    private ImagesActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(ImagesActivity.class).create().get();
    }

    @Test
    public void whenNavigateToEditIsCalled_thenEditActivityWillBeStarted() {
        //given
        ImageDetails imageDetails = new ImageDetails("ImagePath", "ImageName");
        ShadowActivity shadowActivity = shadowOf(activity);

        //when
        activity.openEditScreen(imageDetails);

        //then
        Intent editActivityIntent = shadowActivity.peekNextStartedActivity();
        ImageDetails editActivityImageDetails = editActivityIntent.getParcelableExtra("extra_image_details");
        assertNotNull(editActivityImageDetails);
        assertEquals("ImagePath", editActivityImageDetails.getPath());
        assertEquals("ImageName", editActivityImageDetails.getName());
        assertEquals(new ComponentName(activity, EditActivity.class), editActivityIntent.getComponent());
    }

    @Test
    public void whenSettingsOptionIsClicked_SettingsActivityIsStarted() {
        //Given
        MenuItem menuItem = mock(MenuItem.class);
        ShadowActivity shadowActivity = shadowOf(activity);
        when(menuItem.getItemId()).thenReturn(R.id.settings);

        //When
        activity.onOptionsItemSelected(menuItem);

        //Then
        Intent settingActivity = shadowActivity.peekNextStartedActivity();
        assertEquals(new ComponentName(activity, SettingsActivity.class), settingActivity.getComponent());
    }

    @Test
    public void whenUnknownOptionItemIsClicked_SettingsActivityIsNotStarted() {
        //Given
        MenuItem menuItem = mock(MenuItem.class);
        ShadowActivity shadowActivity = shadowOf(activity);
        when(menuItem.getItemId()).thenReturn(R.id.value_text);

        //When
        activity.onOptionsItemSelected(menuItem);

        //Then
        Intent settingActivity = shadowActivity.peekNextStartedActivity();
        assertNull(settingActivity);
    }

    @Test
    public void whenCreateOptionsMenuIsCalled_ItReturnsTrue() {
        //Given
        activity = spy(activity);
        Menu menu = new NavigationMenu(activity);

        //When
        boolean isMenuCreated = activity.onCreateOptionsMenu(menu);

        //Then
        assertTrue(isMenuCreated);
    }

    @Test
    public void whenSupportedImageIsClicked_EditActivityIsStarted() {
        //When
        activity.openEditScreen(new ImageDetails("path", "name"));

        //Then
        Intent startedIntent = shadowOf(activity).getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertEquals(EditActivity.class, shadowIntent.getIntentClass());
    }

    @Test
    public void whenImagesActivityBuildIsInitiated_ImagesActivityIntentIsCreated() {
        //Given
        Context context = RuntimeEnvironment.application;
        Intent expectedIntent = new Intent(context, ImagesActivity.class);

        //When
        Intent intent = ImagesActivity.buildIntent(context);

        //Then
        assertThat(intent.getComponent(), is(expectedIntent.getComponent()));
    }

    @Test
    public void whenShowSnackBarIsCalled_ShowOnSnackBarIsCalled() {
        //Given
        ImagesActivity activity = new ImagesActivity();
        Snackbar snackbar = mock(Snackbar.class);
        activity.setSnackbar(snackbar);

        //When
        activity.showSnackBar();

        //Then
        verify(snackbar).show();
    }

    @Test
    public void whenShowSnackBarIsAlreadyCreated_ThereIsNoNewCreation() {
        //Given
        activity.prepareSnackBar(R.string.error_message);
        Snackbar expectedSnackbar = activity.getSnackbar();

        //When
        activity.prepareSnackBar(R.string.error_message);
        Snackbar actualSnackbar = activity.getSnackbar();

        //Then
        assertSame(expectedSnackbar, actualSnackbar);
    }

    @Test
    public void whenStopRefreshingImagesIsCalled_RefreshingIsSetToFalse() {
        //Given
        ImagesActivity activity = new ImagesActivity();
        SwipeRefreshLayout refreshLayout = mock(SwipeRefreshLayout.class);
        activity.setRefreshLayout(refreshLayout);

        //When
        activity.onStopRefreshingImages();

        //Then
        verify(refreshLayout).setRefreshing(false);
    }

    @Test
    public void whenAppIsRunningOnPhone_CreatePhoneBindingIsCalled() {
        //Given
        ImagesActivity activity = Robolectric.buildActivity(ImagesActivity.class).create().get();
        activity = spy(activity);
        WindowManager manager = mock(WindowManager.class);
        Display display = mock(Display.class);

        doReturn(manager).when(activity).getWindowManager();
        when(manager.getDefaultDisplay()).thenReturn(display);
        doAnswer(invocation -> {
            DisplayMetrics displayMetrics = invocation.getArgument(0);
            displayMetrics.heightPixels = 1794;
            displayMetrics.widthPixels = 1080;
            displayMetrics.xdpi = 422f;
            displayMetrics.ydpi = 424f;
            return null;
        }).when(display).getMetrics(any());

        doNothing().when(activity).prepareToolbar();
        doNothing().when(activity).createPhoneBinding();

        //When
        activity.prepareView();

        //Then
        verify(activity).createPhoneBinding();
    }

    @Test
    public void whenAppIsRunningOnTablet_CreateTabletBindingIsCalled() {
        //Given
        ImagesActivity activity = Robolectric.buildActivity(ImagesActivity.class).create().get();
        activity = spy(activity);
        WindowManager manager = mock(WindowManager.class);
        Display display = mock(Display.class);

        doReturn(manager).when(activity).getWindowManager();
        when(manager.getDefaultDisplay()).thenReturn(display);
        doAnswer(invocation -> {
            DisplayMetrics displayMetrics = invocation.getArgument(0);
            displayMetrics.heightPixels = 6048;
            displayMetrics.widthPixels = 6536;
            displayMetrics.xdpi = 30f;
            displayMetrics.ydpi = 31f;
            return null;
        }).when(display).getMetrics(any());

        doNothing().when(activity).prepareToolbar();
        doNothing().when(activity).createTabletBinding();

        //When
        activity.prepareView();

        //Then
        verify(activity).createTabletBinding();
    }

    @Test
    public void whenOnRejectTagsChangesDialogPositiveIsCalled_OnRejectTagsChangesDialogPositiveIsCalled() {
        //Given
        ImagesActivity activity = spy(ImagesActivity.class);
        TabletImagesViewModel tabletImagesViewModel = mock(TabletImagesViewModel.class);
        activity.setTabletImagesViewModel(tabletImagesViewModel);

        //When
        activity.onRejectTagsChangesDialogPositive();

        //Then
        verify(tabletImagesViewModel).onRejectTagsChangesDialogPositive();
    }

    @Test
    public void whenChangedViewToDefaultModeIsCalledAndThereIsNoViewModel_NothingIsDone() {
        //Given
        ImagesActivity activity = spy(ImagesActivity.class);

        //When
        activity.changeViewToDefaultMode();

        //Then
        //Error not thrown
    }

    @Test
    public void whenChangedViewToDefaultModeIsCalledAndThere_ChangeViewToDefaultModeIsCalled() {
        //Given
        ImagesActivity activity = spy(ImagesActivity.class);
        TabletEditViewModel editViewModel = mock(TabletEditViewModel.class);
        activity.setTabletEditViewModel(editViewModel);

        //When
        activity.changeViewToDefaultMode();

        //Then
        verify(editViewModel).changeViewToDefaultMode();
    }

    @Test
    public void whenShowEditViewIsCalled_EditViewModelIsInitialized() throws IOException {
        //Given
        ImagesActivity activity = spy(ImagesActivity.class);
        TabletEditViewModel editViewModel = mock(TabletEditViewModel.class);
        activity.setTabletEditViewModel(editViewModel);
        doReturn(mock(TagsManager.class)).when(activity).buildTagsManager(any());

        doNothing().when(editViewModel).initialize(any(), any(), any());

        //When
        activity.showEditView(mock(ImageDetails.class));

        //Then
        verify(editViewModel).initialize(any(), any(), any());
    }

    @Test
    public void whenShareImageIsCalled_IntentChooserWithSharingImageIsCalled() throws FileNotFoundException {
        //Given
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        SharingHelper helper = spy(new SharingHelper(activity.getContentResolver()));
        ImageDetails imageDetails = new ImageDetails("Path", "Name");
        doReturn("media:content").when(helper).prepareImagePathToShare(imageDetails, activity.getContentResolver());
        Intent shareIntent = helper.buildShareImageIntent(imageDetails);

        //When
        activity.onShare(shareIntent);
        Intent startedShareIntent = shadowActivity.getNextStartedActivity();

        //Then
        assertThat(startedShareIntent, is(notNullValue()));
    }

    @Test
    public void whenShareImageIsCalledAndShareIntentDoNotHaveExtras_IntentChooserWithSharingImageIsCalled() throws FileNotFoundException {
        //Given
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        SharingHelper helper = spy(new SharingHelper(activity.getContentResolver()));
        ImageDetails imageDetails = new ImageDetails("Path", "Name");
        doReturn("media:content").when(helper).prepareImagePathToShare(imageDetails, activity.getContentResolver());
        when(helper.buildShareImageIntent(imageDetails)).thenReturn(new Intent(Intent.ACTION_SEND));
        Intent shareIntent = helper.buildShareImageIntent(imageDetails);

        //When
        activity.onShare(shareIntent);
        Intent startedShareIntent = shadowActivity.getNextStartedActivity();

        //Then
        assertThat(startedShareIntent, is(notNullValue()));
    }
}
