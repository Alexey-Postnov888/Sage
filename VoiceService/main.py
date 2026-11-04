import tempfile
import os
from fastapi import FastAPI, UploadFile, File, HTTPException
from faster_whisper import WhisperModel

app = FastAPI(title="Voice Recognition Service")

print("Загрузка модели Whisper...")

model = WhisperModel("small", device="cpu",compute_type="int8")
print("Модель загружена!")

@app.post("/api/voice/recognize")
async def recognize(file: UploadFile = File(...)):
    if not file:
        raise HTTPException(status_code=400, detail = "Файл не передан")

    filename = file.filename or "audio.wav"
    suffix = os.path.splitext(filename)[1]

    try:
        with tempfile.NamedTemporaryFile(delete=False, suffix=suffix) as temp_file:
            content = await file.read()
            temp_file.write(content)
            temp_path = temp_file.name

        segments, info = model.transcribe(temp_path, language="ru", beam_size=5)

        text = " ".join([segment.text for segment in segments]).strip()

        return {"text": text}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Ошибка распознавания: {str(e)}")

    finally:
        if "temp_path" in locals() and os.path.exists(temp_path):
            os.remove(temp_path)
