package com.example.funcionalidadesbluetooth;

import static org.junit.Assert.*;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BluetoothServiceTest {
    @Mock Context mockContext;
    @Mock BluetoothManager mockBluetoothManager;
    @Mock BluetoothAdapter mockBluetoothAdapter;
    @Mock BluetoothProfile mockBluetoothProfile;

    BluetoothService bluetoothService;
    MockedStatic<Toast> toastMockedStatic;

    MockedConstruction<Intent> intentMockedConstruction;
    Intent mockIntent;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bluetoothService = new BluetoothService();

        // Mock Toast.makeText
        toastMockedStatic = mockStatic(Toast.class);
        toastMockedStatic.when(() -> Toast.makeText(any(), anyString(), anyInt()))
                .thenReturn(mock(Toast.class));

        // Mock Intent constructor y addFlags
        intentMockedConstruction = mockConstruction(Intent.class, (mock, context) -> {
            mockIntent = mock;
            when(mock.addFlags(anyInt())).thenReturn(mock); // addFlags devuelve el propio Intent
            when(mock.getAction()).thenReturn(Settings.ACTION_BLUETOOTH_SETTINGS);
        });
    }

    @After
    public void tearDown() {
        if (toastMockedStatic != null) toastMockedStatic.close();
        if (intentMockedConstruction != null) intentMockedConstruction.close();
    }

    @Test
    public void testBluetoothNotSupported() {
        when(mockContext.getSystemService(Context.BLUETOOTH_SERVICE)).thenReturn(null);

        bluetoothService.bluetoothEncendido(mockContext);

        verify(mockContext, never()).startActivity(any());
    }

    @Test
    public void testBluetoothOff() {
        when(mockContext.getSystemService(Context.BLUETOOTH_SERVICE)).thenReturn(mockBluetoothManager);
        when(mockBluetoothManager.getAdapter()).thenReturn(mockBluetoothAdapter);
        when(mockBluetoothAdapter.isEnabled()).thenReturn(false);

        bluetoothService.bluetoothEncendido(mockContext);

        verify(mockContext).startActivity(argThat(intent ->
                Settings.ACTION_BLUETOOTH_SETTINGS.equals(intent.getAction())
        ));
    }

    @Test
    public void testBluetoothOnAndConnected() {
        when(mockContext.getSystemService(Context.BLUETOOTH_SERVICE)).thenReturn(mockBluetoothManager);
        when(mockBluetoothManager.getAdapter()).thenReturn(mockBluetoothAdapter);
        when(mockBluetoothAdapter.isEnabled()).thenReturn(true);

        doAnswer(invocation -> {
            BluetoothProfile.ServiceListener listener = invocation.getArgument(1);
            when(mockBluetoothProfile.getConnectedDevices()).thenReturn(Collections.singletonList(mock(BluetoothDevice.class)));
            listener.onServiceConnected(BluetoothProfile.HEADSET, mockBluetoothProfile);
            return null;
        }).when(mockBluetoothAdapter).getProfileProxy(any(), any(), eq(BluetoothProfile.HEADSET));

        bluetoothService.bluetoothEncendido(mockContext);

        verify(mockContext, never()).startActivity(any());
    }

    @Test
    public void testBluetoothOnAndNotConnected() {
        when(mockContext.getSystemService(Context.BLUETOOTH_SERVICE)).thenReturn(mockBluetoothManager);
        when(mockBluetoothManager.getAdapter()).thenReturn(mockBluetoothAdapter);
        when(mockBluetoothAdapter.isEnabled()).thenReturn(true);

        doAnswer(invocation -> {
            BluetoothProfile.ServiceListener listener = invocation.getArgument(1);
            when(mockBluetoothProfile.getConnectedDevices()).thenReturn(Collections.emptyList());
            listener.onServiceConnected(BluetoothProfile.HEADSET, mockBluetoothProfile);
            return null;
        }).when(mockBluetoothAdapter).getProfileProxy(any(), any(), eq(BluetoothProfile.HEADSET));

        bluetoothService.bluetoothEncendido(mockContext);

        verify(mockContext).startActivity(argThat(intent ->
                Settings.ACTION_BLUETOOTH_SETTINGS.equals(intent.getAction())
        ));
    }

}