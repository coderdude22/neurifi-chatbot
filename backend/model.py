from transformers import AutoModelForCausalLM, AutoTokenizer
import torch

class ChatbotModel:
    def __init__(self, model_name = "google/gemma-2b"):
        print("Loading model... (this may take a few minutes, grab a coffee)")

        try:
            # Load tokenizer
            self.tokenizer = AutoTokenizer.from_pretrained(model_name)
            self.tokenizer.pad_token = self.tokenizer.eos_token  # Use EOS token as padding token
            print("Tokenizer loaded!")    
             

            # Load model
            self.model = AutoModelForCausalLM.from_pretrained(
                model_name,   
                torch_dtype=torch.float32,             
                device_map="cpu" # Force CPU usage                
            )

            print("Model loaded successfully!")

        except Exception as e:
            print(f"Error loading model: {e}")

    def generate_response(self, user_input):
        inputs = self.tokenizer(user_input, return_tensors="pt", padding=True, truncation=True, max_length=512)  
    
        attention_mask = inputs["attention_mask"]  
    
        response_ids = self.model.generate(
            inputs["input_ids"], 
            max_new_tokens=50,
            temperature=0.7,
            do_sample=True,
            top_k=50,
            top_p=0.9,
            pad_token_id=self.tokenizer.pad_token_id,  
            attention_mask=attention_mask  
    )  

        response = self.tokenizer.decode(response_ids[:, inputs["input_ids"].shape[-1]:][0], skip_special_tokens=True)
        return response  

# Run the chatbot if script is executed directly
if __name__ == "__main__":
    chatbot = ChatbotModel()

    print("Chatbot is ready! Type your message below.")
    print("Type 'exit' or 'quit' to stop.")

    while True:
        user_input = input("You: ")  # Get user input
        if user_input.lower() in ["exit", "quit", "bye"]:  # Allow the user to exit
            print("Goodbye!")
            break
        response = chatbot.generate_response(user_input)
        print("Bot:", response)

