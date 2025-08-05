package com.confluence.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.confluence.ui.components.PreviewDialog
import com.confluence.ui.components.RealtimeConvertor
import com.confluence.ui.components.StatusMessage
import de.felixlf.confluencemd.core.ConfluenceWriter
import de.felixlf.confluencemd.core.FileOperations
import kotlinx.coroutines.launch

@Composable
fun App(fileOperations: FileOperations) {
    MaterialTheme {
        val confluenceWriter = remember { ConfluenceWriter(fileOperations) }
        val coroutineScope = rememberCoroutineScope()
        val (inputPath, setInputPath) = remember { mutableStateOf("") }
        val (outputPath, setOutputPath) = remember { mutableStateOf("") }
        val (isConverting, setIsConverting) = remember { mutableStateOf(false) }
        val (conversionStatus: ConversionStatus?, setConversionStatus: (ConversionStatus?) -> Unit) = remember {
            mutableStateOf<ConversionStatus?>(
                null
            )
        }
        val (showPreview, setShowPreview) = remember { mutableStateOf(false) }
        val (previewText, setPreviewText) = remember { mutableStateOf("") }
        val (showRealtimeConvertor, setShowRealtimeConvertor) = remember { mutableStateOf(false) }

        LaunchedEffect(inputPath, outputPath) {
            if (inputPath.isNotEmpty() && outputPath.isEmpty()) {
                setOutputPath(inputPath.substringBeforeLast(".") + ".xml")
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // App Title
                Text(
                    text = "Confluence Writer",
                    style = MaterialTheme.typography.h5
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input file selection
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = inputPath,
                        onValueChange = setInputPath,
                        label = { Text("Source Markdown File") },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        selectInputFile(setInputPath)
                    }) {
                        Text("Browse")
                    }
                }

                // Output file selection
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = outputPath,
                        onValueChange = (setOutputPath),
                        label = { Text("Output Confluence XML File") },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        selectOutputFile(setOutputPath)
                    }) {
                        Text("Browse")
                    }
                }

                // Preview button, Realtime Convertor button and Convert button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    setIsConverting(true)
                                    val markdown = fileOperations.readFile(inputPath)
                                    setPreviewText(confluenceWriter.convertString(markdown))
                                    setShowPreview(true)
                                } catch (e: Exception) {
                                    setConversionStatus(ConversionStatus.Error("Error reading file: ${e.message}"))
                                } finally {
                                    setIsConverting(false)
                                }
                            }
                        },
                        enabled = inputPath.isNotEmpty() && !isConverting
                    ) {
                        Text("Preview")
                    }

                    Button(
                        onClick = {
                            setShowRealtimeConvertor(true)
                        }
                    ) {
                        Text("Realtime Convertor")
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                setIsConverting(true)
                                val success = confluenceWriter.convertFile(inputPath, outputPath)
                                setConversionStatus(
                                    when {
                                        success -> ConversionStatus.Success("File converted successfully!")
                                        else -> ConversionStatus.Error("Failed to convert file")
                                    }
                                )
                                setIsConverting(false)
                            }
                        },
                        enabled = inputPath.isNotEmpty() && outputPath.isNotEmpty() && !isConverting,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isConverting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colors.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text("Convert")
                    }
                }

                // Status message
                conversionStatus?.let { StatusMessage(it) }

                PreviewDialog(
                    visible = showPreview,
                    previewText = previewText,
                    setShowPreview = setShowPreview
                )

                RealtimeConvertor(
                    visible = showRealtimeConvertor,
                    confluenceWriter = confluenceWriter,
                    setShowRealtimeConvertor = setShowRealtimeConvertor
                )
            }
        }
    }
}


// Platform-specific file selectors
expect fun selectInputFile(onFileSelected: (String) -> Unit)
expect fun selectOutputFile(onFileSelected: (String) -> Unit)

// Platform-specific clipboard function
expect fun copyToClipboardImpl(text: String)

@Composable
expect fun PreviewDialog(
    visible: Boolean,
    onCloseRequest: () -> Unit,
    previewText: String,
    content: @Composable () -> Unit
)

@Composable
expect fun RealtimeConvertorDialog(
    visible: Boolean,
    onCloseRequest: () -> Unit,
    content: @Composable () -> Unit
)