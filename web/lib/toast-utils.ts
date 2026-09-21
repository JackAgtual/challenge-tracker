import { toast } from "@/components/ui/toast";

export function toastGenericError() {
  toast.add({
    title: "Error",
    description: "Something went wrong. Try again.",
    type: "error",
  });
}
