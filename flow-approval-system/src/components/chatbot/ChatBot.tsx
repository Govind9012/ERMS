// import React, { useState } from "react";

// type Message = {
//   role: "user" | "assistant";
//   content: string;
// };

// const ChatBot: React.FC = () => {
//   const [messages, setMessages] = useState<Message[]>([]);
//   const [input, setInput] = useState("");
//   const [loading, setLoading] = useState(false);

//   const sendMessage = async () => {
//     if (!input.trim()) return;

//     const userMessage: Message = { role: "user", content: input };
//     const updatedMessages = [...messages, userMessage];
//     setMessages(updatedMessages);
//     setInput("");
//     setLoading(true);

//     try {
//       const response = await fetch(
//         `${process.env.REACT_APP_AZURE_OPENAI_ENDPOINT}/openai/deployments/${process.env.REACT_APP_AZURE_OPENAI_DEPLOYMENT}/chat/completions?api-version=${process.env.REACT_APP_AZURE_OPENAI_API_VERSION}`,
//         {
//           method: "POST",
//           headers: {
//             "Content-Type": "application/json",
//             "api-key": process.env.REACT_APP_AZURE_OPENAI_API_KEY || "",
//           },
//           body: JSON.stringify({
//             messages: updatedMessages,
//             temperature: 0.7,
//             max_tokens: 500,
//           }),
//         }
//       );

//       const data = await response.json();
//       const assistantMessage: Message = {
//         role: "assistant",
//         content: data.choices[0].message.content,
//       };

//       setMessages([...updatedMessages, assistantMessage]);
//     } catch (error) {
//       console.error("Azure OpenAI Error:", error);
//     } finally {
//       setLoading(false);
//     }
//   };

//   return (
//     <div style={styles.container}>
//       <div style={styles.chatBox}>
//         {messages.map((msg, index) => (
//           <div
//             key={index}
//             style={{
//               ...styles.message,
//               alignSelf: msg.role === "user" ? "flex-end" : "flex-start",
//               background:
//                 msg.role === "user" ? "#DCF8C6" : "#EAEAEA",
//             }}
//           >
//             {msg.content}
//           </div>
//         ))}
//         {loading && <div style={styles.typing}>Typing...</div>}
//       </div>

//       <div style={styles.inputBox}>
//         <input
//           value={input}
//           onChange={(e) => setInput(e.target.value)}
//           placeholder="Type your message..."
//           style={styles.input}
//           onKeyDown={(e) => e.key === "Enter" && sendMessage()}
//         />
//         <button onClick={sendMessage} style={styles.button}>
//           Send
//         </button>
//       </div>
//     </div>
//   );
// };

// export default ChatBot;

// const styles: { [key: string]: React.CSSProperties } = {
//   container: {
//     width: "100%",
//     maxWidth: "500px",
//     height: "600px",
//     border: "1px solid #ccc",
//     display: "flex",
//     flexDirection: "column",
//     borderRadius: "8px",
//   },
//   chatBox: {
//     flex: 1,
//     padding: "10px",
//     overflowY: "auto",
//     display: "flex",
//     flexDirection: "column",
//     gap: "8px",
//   },
//   message: {
//     padding: "10px",
//     borderRadius: "6px",
//     maxWidth: "80%",
//     fontSize: "14px",
//   },
//   typing: {
//     fontStyle: "italic",
//     fontSize: "12px",
//   },
//   inputBox: {
//     display: "flex",
//     borderTop: "1px solid #ccc",
//   },
//   input: {
//     flex: 1,
//     padding: "10px",
//     border: "none",
//     outline: "none",
//   },
//   button: {
//     padding: "10px 16px",
//     border: "none",
//     cursor: "pointer",
//     background: "#1976d2",
//     color: "#fff",
//   },
// };
