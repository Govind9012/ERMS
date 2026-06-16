import { useState } from "react";
import { MessageCircle, Send, X, Bot } from "lucide-react";

type Message = {
  role: "user" | "assistant";
  content: string;
};

const ChatBotWidget = () => {
  const [open, setOpen] = useState(false);
  const [messages, setMessages] = useState<Message[]>([
    {
      role: "assistant",
      content: "Hi! I'm your AI assistant. How can I help you today?",
    },
  ]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);

  const sendMessage = async () => {
    if (!input.trim()) return;

    const userMessage: Message = { role: "user", content: input };
    const updatedMessages = [...messages, userMessage];

    setMessages(updatedMessages);
    setInput("");
    setLoading(true);

    try {
      const response = await fetch(
        `${import.meta.env.VITE_AZURE_OPENAI_ENDPOINT}/openai/deployments/${import.meta.env.VITE_AZURE_OPENAI_DEPLOYMENT}/chat/completions?api-version=${import.meta.env.VITE_AZURE_OPENAI_API_VERSION}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "api-key": import.meta.env.VITE_AZURE_OPENAI_API_KEY,
          },
          body: JSON.stringify({
            messages: updatedMessages,
            temperature: 0.7,
            max_tokens: 500,
          }),
        }
      );

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText);
      }

      const data = await response.json();

      const assistantMessage: Message = {
        role: "assistant",
        content: data.choices[0].message.content,
      };

      setMessages([...updatedMessages, assistantMessage]);
    } catch (err) {
      console.error("Azure OpenAI Error:", err);
      setMessages([
        ...updatedMessages,
        {
          role: "assistant",
          content: "Unable to connect to AI service.",
        },
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {!open && (
        <button
          onClick={() => setOpen(true)}
          className="fixed bottom-6 right-6 w-14 h-14 rounded-full bg-blue-600 text-white shadow-lg flex items-center justify-center"
        >
          <MessageCircle className="w-6 h-6" />
        </button>
      )}

      {open && (
        <div className="fixed bottom-6 right-6 w-[380px] h-[520px] bg-white rounded-2xl shadow-2xl flex flex-col border">
          {/* Header */}
          <div className="bg-blue-600 text-white px-4 py-3 flex items-center justify-between rounded-t-2xl">
            <div className="flex items-center gap-2">
              <Bot className="w-5 h-5" />
              <div>
                <p className="font-semibold leading-none">AI Assistant</p>
                <span className="text-xs opacity-80">Powered by Azure OpenAI</span>
              </div>
            </div>
            <button onClick={() => setOpen(false)}>
              <X className="w-5 h-5" />
            </button>
          </div>

          {/* Messages */}
          <div className="flex-1 p-4 space-y-3 overflow-y-auto">
            {messages.map((msg, i) => (
              <div
                key={i}
                className={`max-w-[80%] px-3 py-2 rounded-lg text-sm ${
                  msg.role === "user"
                    ? "ml-auto bg-blue-600 text-white"
                    : "bg-gray-100 text-gray-900"
                }`}
              >
                {msg.content}
              </div>
            ))}
            {loading && (
              <div className="text-xs text-gray-400">AI is typing...</div>
            )}
          </div>

          {/* Input */}
          <div className="p-3 border-t flex gap-2">
            <input
              value={input}
              onChange={(e) => setInput(e.target.value)}
              placeholder="Type a message..."
              className="flex-1 px-3 py-2 border rounded-lg text-sm outline-none"
              onKeyDown={(e) => e.key === "Enter" && sendMessage()}
            />
            <button
              onClick={sendMessage}
              className="bg-blue-600 text-white px-3 rounded-lg flex items-center justify-center"
            >
              <Send className="w-4 h-4" />
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default ChatBotWidget;
